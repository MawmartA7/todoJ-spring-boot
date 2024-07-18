package br.com.todo.todo.dto.errors;

public record DefaultErrorMessageDTO(Integer statusCode, String message, String description) {
}