package com.cookbook.exceptions;

public class EntityNotFoundException extends ServiceRuntimeException {

    public EntityNotFoundException(Class<?> clazz, long id) {
        super(String.format("There's no valid %s with id %s", clazz.getSimpleName(), id));
    }
}
