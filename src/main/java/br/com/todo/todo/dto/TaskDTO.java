package br.com.todo.todo.dto;

import br.com.todo.todo.models.Task;
import jakarta.validation.constraints.*;

public record TaskDTO(
        Long id, @NotBlank(message = "Name must be filled with characters") String name,
        @NotBlank(message = "Description must be filled with characters") String description,
        @NotNull(message = "Priority cannot be null") @Positive(message = "Priority cannot be negative or zero") Integer priority,
        @NotNull(message = "Done cannot be null") Boolean done) {

    public TaskDTO(Task data) {
        this(data.getId(), data.getName(), data.getDescription(), data.getPriority(), data.getDone());
    }

}