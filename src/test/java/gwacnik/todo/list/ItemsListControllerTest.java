package gwacnik.todo.list;

import gwacnik.todo.item.Item;
import gwacnik.todo.item.ItemService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import static gwacnik.todo.item.ItemTestUtils.todo;
import static gwacnik.todo.list.ItemsList.ItemsListBuilder;
import static gwacnik.todo.list.ItemsListController.URL_MAPPING_LIST;
import static gwacnik.todo.list.ItemsListController.URL_MAPPING_LIST_ADD_ITEM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class ItemsListControllerTest
{
    private static final String SHOPPING_LIST = "Shopping list";
    private static final String MILK = "Milk";
    private static final String MEAT = "Meat";
    private static final String APPLES = "Apples";
    private static final String CHEESE = "Cheese";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemsListService itemsListService;

    @Autowired
    private ItemService itemService;

    @Test
    public void shouldReturnEmptyIfNoListsCreated() throws Exception
    {
        mockMvc.perform(get(URL_MAPPING_LIST))
              .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnListsOfItems() throws Exception
    {
        final String doShopping = "Do Shopping";
        itemsListService.save(todoList("ToDo this Month", todo(doShopping), todo("Insurance")));
        itemsListService.save(todoList("Not so urgent things", todo("Create a blog")));

        mockMvc.perform(get(URL_MAPPING_LIST))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$", hasSize(2)))
              .andExpect(jsonPath("$.[0].items", hasSize(2)))
              .andExpect(jsonPath("$.[0].items[0].content", is(doShopping)));
    }

    @Test
    public void shouldReturnNotFoundByUUidIfItemDoesNotExists() throws Exception
    {
        mockMvc.perform(get(URL_MAPPING_LIST + "/" + UUID.randomUUID()))
              .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnItemByUuid() throws Exception
    {
        final UUID uuid = UUID.randomUUID();
        itemsListService.save(todoList("One more thing", uuid));

        mockMvc.perform(get(URL_MAPPING_LIST + "/" + uuid.toString()))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id", is(uuid.toString())));
    }

    @Test
    public void shouldCreateNewItemsList() throws Exception
    {
        final MvcResult result = mockMvc.perform(post(URL_MAPPING_LIST)
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(shoppingList())))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").isNotEmpty())
              .andExpect(jsonPath("$.name", is(SHOPPING_LIST)))
              .andExpect(jsonPath("$.items", hasSize(3)))
              .andReturn();

        final ItemsList responseList = readFrom(result);
        final Optional<ItemsList> persistedList = itemsListService.byId(responseList.getId());

        assertThat(persistedList).map(ItemsList::getName).contains(SHOPPING_LIST);
        assertThat(itemsOf(persistedList)).extracting(Item::getContent).contains(APPLES, MEAT, MILK);
    }

    @Test
    public void shouldAddItemToTheList() throws Exception
    {
        final ItemsList shoppingList = itemsListService.save(shoppingList());
        final UUID newItemId = itemService.save(todo(CHEESE)).getId();

        final MvcResult result = mockMvc.perform(post(URL_MAPPING_LIST + "/" + shoppingList.getId() + URL_MAPPING_LIST_ADD_ITEM + "/" + newItemId))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.items", hasSize(4)))
              .andReturn();

        final ItemsList responseList = readFrom(result);
        final Optional<ItemsList> persistedList = itemsListService.byId(responseList.getId());

        assertThat(itemsOf(persistedList)).extracting(Item::getContent).contains(APPLES, MEAT, MILK, CHEESE);
    }

    private ItemsList shoppingList()
    {
        return todoList(SHOPPING_LIST, todo(MILK), todo(MEAT), todo(APPLES));
    }

    private ItemsList todoList(final String name, Item... items)
    {
        return todoList(name, UUID.randomUUID(), items);
    }

    private ItemsList todoList(final String name, final UUID uuid, Item... items)
    {
        final List<Item> listItems = Arrays.asList(items);
        listItems.forEach(itemService::save);
        return new ItemsListBuilder().setId(uuid).setName(name).setItems(listItems).createItemsList();
    }

    private ItemsList readFrom(final MvcResult result) throws java.io.IOException
    {
        return new ObjectMapper().readValue(result.getResponse().getContentAsString(), ItemsList.class);
    }

    private List<Item> itemsOf(final Optional<ItemsList> persistedList)
    {
        return persistedList.map(ItemsList::getItems).orElse(Collections.emptyList());
    }
}
