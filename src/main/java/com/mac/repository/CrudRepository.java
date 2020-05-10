package com.mac.repository;

import java.util.List;

public interface CrudRepository <T, K> {

    List<T> findAll(); //Read (Select *)
    void save(T t); // Create and Update (INSERT and UPDATE)
    void deleteById(K id); // Delete from WHERE id = "id"
    T findById(K id); //SELECT * from TABLE WHERE id = "id"

}
