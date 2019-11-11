package gwacnik.todo.item;

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
@RequestMapping(ItemController.URL_MAPPING_ITEM)
public class ItemController
{
    static final String URL_MAPPING_ITEM = "/item";
    private ItemService itemService;

    public ItemController(final ItemService itemService)
    {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity getAll()
    {
        return new ResponseEntity<>(itemService.all(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getByUuid(@PathVariable("id") UUID id)
    {
        return itemService.byId(id).map(toOkResponse()).orElse(notFound());
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Item item)
    {
        return new ResponseEntity<>(itemService.save(item), HttpStatus.OK);
    }

    private ResponseEntity<Item> notFound()
    {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private Function<Item, ResponseEntity<Item>> toOkResponse()
    {
        return item -> new ResponseEntity<>(item, HttpStatus.OK);
    }
}
