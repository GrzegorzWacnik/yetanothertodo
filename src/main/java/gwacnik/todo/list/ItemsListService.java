package gwacnik.todo.list;

import gwacnik.todo.item.Item;
import gwacnik.todo.item.ItemService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;


@Service
public class ItemsListService
{
    private ItemsListRepository itemsListRepository;
    private ItemService itemService;

    public ItemsListService(final ItemsListRepository itemsListRepository, final ItemService itemService)
    {
        this.itemsListRepository = itemsListRepository;
        this.itemService = itemService;
    }

    public Iterable<ItemsList> all()
    {
        return itemsListRepository.findAll();
    }

    public Optional<ItemsList> byId(final UUID id)
    {
        return itemsListRepository.findById(id);
    }

    public ItemsList save(final ItemsList todoList)
    {
        return itemsListRepository.save(todoList);
    }

    public ItemsList add(final UUID listID, final UUID itemId)
    {
        final ItemsList itemsList = itemsListRepository.findById(listID).orElseThrow(() -> new IllegalArgumentException("Items List not found for UUID " + listID));
        final List<Item> items = itemsList.getItems();

        itemService.byId(itemId).ifPresent(items::add);

        return itemsListRepository.save(itemsList);
    }
}
