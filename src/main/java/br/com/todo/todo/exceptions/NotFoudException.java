package br.com.todo.todo.exceptions;

import lombok.Getter;

@Getter
public class NotFoudException extends RuntimeException {
    String details;

    public NotFoudException() {
        super("Task not found");
        this.details = "the task could not be found or recovered";
    }

    public NotFoudException(String message, String details) {
        super(message);
        this.details = details;
    }
}
