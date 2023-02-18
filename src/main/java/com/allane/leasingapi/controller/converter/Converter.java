package com.allane.leasingapi.controller.converter;

public interface Converter<S, T> {

    T convert(S source);
}
