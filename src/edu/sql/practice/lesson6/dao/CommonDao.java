package edu.sql.practice.lesson6.dao;

import java.util.List;
import java.util.Optional;

public interface CommonDao<T> {

    Optional<T> findOne(Long id);

    List<T> findAll();

    Optional<T> create(T source);

    Optional<T> update(T source);

    void remove(Long id);

}
