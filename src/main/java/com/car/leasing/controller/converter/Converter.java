package com.car.leasing.controller.converter;

public interface Converter<S, T> {

    T convert(S source);
}
