package br.com.todo.todo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.todo.todo.dto.TaskDTO;
import br.com.todo.todo.services.TaskServices;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<?> postCreateTask(@RequestBody @Valid TaskDTO taskDTO) {
        return services.postCreateTask(taskDTO);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> putUpdateTask(@RequestBody @Valid TaskDTO taskDTO, @PathVariable Long taskId) {
        return services.putUpdateTask(taskDTO, taskId);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<?> patchPartialUpdateTask(@RequestBody TaskDTO taskDTO, @PathVariable Long taskId) {
        return services.patchPartialUpdateTask(taskDTO, taskId);
    }
}
