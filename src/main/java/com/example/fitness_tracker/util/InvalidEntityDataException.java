package com.example.fitness_tracker.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEntityDataException extends RuntimeException {
    public InvalidEntityDataException(String message) {
        super(message);
    }
}