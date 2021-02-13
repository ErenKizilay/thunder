package org.thunder.converter;

public interface EventConverter<T, R> {

    R convert(T event);
}
