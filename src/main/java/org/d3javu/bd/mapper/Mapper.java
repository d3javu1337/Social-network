package org.d3javu.bd.mapper;

public interface Mapper<F, T> {

    T map(F object);

    default T map(F from, T to){
        return to;
    }

}
