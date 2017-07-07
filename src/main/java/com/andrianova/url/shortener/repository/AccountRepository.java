package com.andrianova.url.shortener.repository;

import java.util.List;

/**
 * Created by natal on 09-Jun-17.
 */
public interface AccountRepository<T> {

    T get(int id);

    List<T> getAll();

    void insert(T entity);

    T getByLogin(String login);
}
