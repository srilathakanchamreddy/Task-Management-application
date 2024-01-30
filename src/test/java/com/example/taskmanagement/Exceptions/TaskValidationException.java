package com.example.taskmanagement.Exceptions;

import java.util.List;

public class TaskValidationException extends RuntimeException {

    private List<String> errors;

    public TaskValidationException(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}

