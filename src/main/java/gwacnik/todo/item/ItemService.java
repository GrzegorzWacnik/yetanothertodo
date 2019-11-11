package gwacnik.todo.item;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;


@Service
public class ItemService
{
    private ItemRepository itemRepository;


    public ItemService(final ItemRepository itemRepository)
    {
        this.itemRepository = itemRepository;
    }

    public Iterable<Item> all()
    {
        return itemRepository.findAll();
    }

    public Optional<Item> byId(final UUID id)
    {
        return itemRepository.findById(id);
    }

    public Item save(final Item item)
    {
        return itemRepository.save(item);
    }
}
