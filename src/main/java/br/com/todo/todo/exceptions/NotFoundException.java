package br.com.todo.todo.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    String details;

    public NotFoundException() {
        super("Task not found");
        this.details = "the task could not be found or recovered";
    }

    public NotFoundException(String message, String details) {
        super(message);
        this.details = details;
    }
}
