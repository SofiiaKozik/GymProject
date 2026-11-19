package com.task.gymproject.exeption;

public class EntityNotFoundExeption extends RuntimeException {

    public EntityNotFoundExeption(final String message) {
        super(message);
    }

}
