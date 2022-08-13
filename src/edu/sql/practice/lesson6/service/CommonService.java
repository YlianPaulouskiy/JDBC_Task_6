package edu.sql.practice.lesson6.service;

import java.util.List;

public interface CommonService<T> {

    T findOne(Long id);

    List<T> findAll();

    T save(T source);

    void remove(Long id);
}
