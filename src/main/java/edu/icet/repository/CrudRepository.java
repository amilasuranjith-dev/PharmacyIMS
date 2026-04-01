package edu.icet.repository;

import java.util.List;

public interface CrudRepository<T, ID> extends SuperRepository {
    boolean save(T entity);
    boolean update(T entity);
    boolean delete(ID id);
    T findById(ID id);
    List<T> findAll();
}
