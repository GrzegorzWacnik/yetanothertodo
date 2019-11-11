package gwacnik.todo.list;

import gwacnik.todo.item.Item;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonAutoDetect;


@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class ItemsList
{
    @Id
    private UUID id;
    private String name;
    @OneToMany
    private List<Item> items;

    public ItemsList()
    {
    }

    private ItemsList(final UUID id, final String name, final List<Item> items)
    {
        this.id = id;
        this.name = name;
        this.items = items;
    }


    public static class ItemsListBuilder
    {
        private UUID id;
        private String name;
        private List<Item> items;

        public ItemsList createItemsList()
        {
            return new ItemsList(id, name, items);
        }

        public ItemsListBuilder setId(final UUID id)
        {
            this.id = id;
            return this;
        }

        public ItemsListBuilder setName(final String name)
        {
            this.name = name;
            return this;
        }

        public ItemsListBuilder setItems(final List<Item> items)
        {
            this.items = items;
            return this;
        }
    }

    public UUID getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public List<Item> getItems()
    {
        return items;
    }
}
