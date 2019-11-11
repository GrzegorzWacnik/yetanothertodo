package gwacnik.todo.item;

import java.util.UUID;


public class ItemTestUtils
{
    private ItemTestUtils()
    {
        throw new UnsupportedOperationException("Not allowed to create an instance");
    }

    public static Item todo(final String content, final UUID uuid)
    {
        return new Item.ItemBuilder().setId(uuid).setContent(content).create();
    }

    public static Item todo(final String content)
    {
        return todo(content, UUID.randomUUID());
    }
}