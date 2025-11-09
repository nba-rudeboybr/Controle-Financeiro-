package com.financeiro.exception;

/**
 * Exceção lançada quando um recurso não é encontrado.
 * Demonstra herança de RuntimeException.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Long id) {
        super(String.format("%s com ID %d não encontrado(a)", resource, id));
    }
}

