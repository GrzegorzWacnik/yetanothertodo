package gwacnik.todo.list;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
interface ItemsListRepository extends CrudRepository<ItemsList, UUID>
{
}
