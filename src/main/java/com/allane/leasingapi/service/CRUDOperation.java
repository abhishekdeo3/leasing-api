package com.allane.leasingapi.service;

public interface CRUDOperation<T, S, N, U> {

    T create(S source);

    T find(N number);

    T update(N number, S source);

    void delete(N number);

    U findAll();
}
