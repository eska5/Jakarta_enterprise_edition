package repository;

import java.util.List;
import java.util.Optional;

public interface Repository<E, K> {
    Optional<E> find(K login);
    List<E> findAll();
    void create(E entity);
    void delete(E entity);
    void update(E entity);
    void detach(E entity);
}
