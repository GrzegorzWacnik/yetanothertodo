package gwacnik.todo.item;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import static gwacnik.todo.item.ItemController.URL_MAPPING_ITEM;
import static gwacnik.todo.item.ItemTestUtils.todo;
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
public class ItemControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemService itemService;

    @Test
    public void shouldReturnEmptyListIfNoItemsCreated() throws Exception
    {
        mockMvc.perform(get(URL_MAPPING_ITEM))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void shouldReturnAllItems() throws Exception
    {
        itemService.save(todo("Something to do"));
        itemService.save(todo("Something important to do"));

        mockMvc.perform(get(URL_MAPPING_ITEM))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void shouldReturnNotFoundByUUidIfItemDoesNotExists() throws Exception
    {
        mockMvc.perform(get(URL_MAPPING_ITEM + "/" + UUID.randomUUID()))
              .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnItemByUuid() throws Exception
    {
        final UUID uuid = UUID.randomUUID();
        itemService.save(todo("One more thing", uuid));

        mockMvc.perform(get(URL_MAPPING_ITEM + "/" + uuid.toString()))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id", is(uuid.toString())));
    }

    @Test
    public void shouldCreateNewItem() throws Exception
    {
        final String itemContent = "Something not important";
        final Item todoItem = todo(itemContent);

        mockMvc.perform(post(URL_MAPPING_ITEM)
              .contentType(MediaType.APPLICATION_JSON)
              .content(new ObjectMapper().writeValueAsString(todoItem)))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").isNotEmpty())
              .andExpect(jsonPath("$.content", is(itemContent)));

        assertThat(itemService.all()).extracting(Item::getContent).contains(itemContent);
    }
}

