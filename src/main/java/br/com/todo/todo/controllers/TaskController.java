package br.com.todo.todo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.todo.todo.dto.TaskDTO;
import br.com.todo.todo.services.TaskServices;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/todo-list")
public class TaskController {
    @Autowired
    TaskServices services;

    @GetMapping
    private ResponseEntity<?> getAllTasks() {
        return services.getAllTasks();
    }

    @GetMapping("/{taskId}")
    private ResponseEntity<?> getTaskById(@PathVariable @Valid @NotNull Long taskId) {
        return services.getTaskById(taskId);
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
    private ResponseEntity<?> postCreateTask(@RequestBody @Valid TaskDTO taskDTO) {
        return services.postCreateTask(taskDTO);
    }

    @PutMapping("/{taskId}")
    private ResponseEntity<?> putUpdateTask(@RequestBody @Valid TaskDTO taskDTO, @PathVariable Long taskId) {
        return services.putUpdateTask(taskDTO, taskId);
    }

    @PatchMapping("/{taskId}")
    private ResponseEntity<?> patchPartialUpdateTask(@RequestBody TaskDTO taskDTO, @PathVariable Long taskId) {
        return services.patchPartialUpdateTask(taskDTO, taskId);
    }

    @DeleteMapping("/{taskId}")
    private ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        return services.deleteTask(taskId);
    }
}
