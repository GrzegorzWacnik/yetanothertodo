package gwacnik.todo.item;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
interface ItemRepository extends CrudRepository<Item, UUID>
{
}
