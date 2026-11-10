package Service;

public interface Service<E> {
    E remove(long id);
    Long generateId();
}
