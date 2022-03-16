package com.dmdev.mapper;

public interface Mapper<F, T> {

    T map(F object);
}
