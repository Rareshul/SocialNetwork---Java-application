package Repository;

import Domain.Entity;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Repository<ID, E extends Entity<ID>> {
    void add(E a);
    void remove(E a);

    E findOne(ID id);
    Iterable<E> getAll();

    default Stream<E> stream() {
        return StreamSupport.stream(getAll().spliterator(), false);
    }

    default List<E> findAll(Predicate<E> filter) {
        return stream().filter(filter).collect(Collectors.toList());
    }
}
