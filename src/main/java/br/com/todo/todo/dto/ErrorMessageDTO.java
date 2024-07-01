package br.com.todo.todo.dto;

import org.springframework.http.HttpStatus;

public record ErrorMessageDTO(HttpStatus statusCode, String message, String details) {
}
