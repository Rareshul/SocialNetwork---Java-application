package Repository;

import Domain.Entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InMemoryRepo<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected Map<ID, E> entities;

    public InMemoryRepo() {
        entities = new HashMap<>();
    }

    @Override
    public void add(E a) {
        if (a == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
        entities.put(a.getIdentitykey(), a);
    }

    @Override
    public void remove(E a) {
        if (a == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
        entities.remove(a.getIdentitykey());
    }

    @Override
    public E findOne(ID id) {
        return entities.get(id);
    }

    @Override
    public Iterable<E> getAll() {
        return Collections.unmodifiableCollection(entities.values());
    }
}
