package gwacnik.todo.item;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonAutoDetect;


@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Item
{
    @Id
    private UUID id;
    private String content;

    public Item()
    {
    }

    private Item(final UUID id, final String content)
    {
        this.id = id;
        this.content = content;
    }

    static class ItemBuilder
    {
        private UUID id;
        private String content;

        Item create()
        {
            return new Item(id, content);
        }

        ItemBuilder setId(final UUID id)
        {
            this.id = id;
            return this;
        }

        ItemBuilder setContent(final String content)
        {
            this.content = content;
            return this;
        }

    }

    public UUID getId()
    {
        return id;
    }

    public String getContent()
    {
        return content;
    }
}
