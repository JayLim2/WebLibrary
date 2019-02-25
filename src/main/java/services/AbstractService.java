package services;

import java.util.List;

public interface AbstractService<E, PK> {
    List<E> getAll();

    E getById(PK id);

    boolean save(E entity);

    boolean update(E entity);

    boolean delete(E entity);
}
