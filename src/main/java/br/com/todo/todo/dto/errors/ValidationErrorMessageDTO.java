package br.com.todo.todo.dto.errors;

import java.util.Map;

public record ValidationErrorMessageDTO(Integer statusCode, String message, Map<String, String> errors) {
}
