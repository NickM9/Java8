package com.trainigcenter.trainee.dao;

public interface Dao<T> {

    void save(T t);
    
    boolean isEntityExist(T t);

}
