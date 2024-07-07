package br.com.todo.todo.controllers;

import br.com.todo.todo.dto.TaskDTO;
import br.com.todo.todo.services.TaskServices;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo-list")
public class TaskController {

    private final TaskServices services;

    public TaskController(TaskServices services) {
        this.services = services;
    }

    @GetMapping
    private ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(services.getAllTasks());
    }

    @GetMapping("/{taskId}")
    private ResponseEntity<TaskDTO> getTaskById(@PathVariable @Valid @NotNull Long taskId) {
        return ResponseEntity.ok(services.getTaskById(taskId));
    }

    @GetMapping("/done")
    private ResponseEntity<?> getAllDoneTasks() {
        return services.getAllDoneTasks();
    }

    @GetMapping("/pending")
    private ResponseEntity<?> getAllPendingTasks() {
        return services.getAllPendingTasks();
    }

    @PostMapping
    private ResponseEntity<TaskDTO> postCreateTask(@RequestBody @Valid TaskDTO taskDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(services.postCreateTask(taskDTO));
    }

    @PutMapping("/{taskId}")
    private ResponseEntity<TaskDTO> putUpdateTask(@RequestBody @Valid TaskDTO taskDTO, @PathVariable Long taskId) {
        return ResponseEntity.ok(services.putUpdateTask(taskDTO, taskId));
    }

    @PatchMapping("/{taskId}")
    private ResponseEntity<TaskDTO> patchPartialUpdateTask(@RequestBody TaskDTO taskDTO, @PathVariable Long taskId) {
        return ResponseEntity.ok(services.patchPartialUpdateTask(taskDTO, taskId));
    }

    @DeleteMapping("/{taskId}")
    private ResponseEntity<Object> deleteTask(@PathVariable Long taskId) {
        services.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
