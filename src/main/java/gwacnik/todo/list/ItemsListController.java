package gwacnik.todo.list;

import java.util.UUID;
import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = ItemsListController.URL_MAPPING_LIST)
public class ItemsListController
{
    static final String URL_MAPPING_LIST = "/list";
    static final String URL_MAPPING_LIST_ADD_ITEM = "/add";
    private static final String ID_MAPPING = "/{id}";

    private ItemsListService itemsListService;

    public ItemsListController(final ItemsListService itemsListService)
    {
        this.itemsListService = itemsListService;
    }

    @GetMapping(value = ID_MAPPING)
    public ResponseEntity getByUuid(@PathVariable("id") UUID id)
    {
        return itemsListService.byId(id).map(toOkResponse()).orElse(notFound());
    }

    @PostMapping
    public ResponseEntity create(@RequestBody ItemsList itemsList)
    {
        return new ResponseEntity<>(itemsListService.save(itemsList), HttpStatus.OK);
    }

    @PostMapping(value = ID_MAPPING + URL_MAPPING_LIST_ADD_ITEM + "/{itemId}")
    public ResponseEntity addItemToTheList(@PathVariable("id") UUID listID, @PathVariable("itemId") UUID itemId)
    {
        return new ResponseEntity<>(itemsListService.add(listID, itemId), HttpStatus.OK);
    }

    private ResponseEntity<ItemsList> notFound()
    {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private Function<ItemsList, ResponseEntity<ItemsList>> toOkResponse()
    {
        return itemsList -> new ResponseEntity<>(itemsList, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getAll()
    {
        return new ResponseEntity<>(itemsListService.all(), HttpStatus.OK);
    }
}
