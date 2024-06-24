package br.com.todo.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskDTO(
        Long id, @NotBlank String name, @NotBlank String description, @NotNull Integer priority,
        @NotNull Boolean done) {
}