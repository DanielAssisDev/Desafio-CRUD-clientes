package com.danielassisdeveloper.atividade3.services.exceptions;

public class ResourceNotFoundException extends RuntimeException     {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
