package br.com.todo.todo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.todo.todo.dto.TaskDTO;
import br.com.todo.todo.services.TaskServices;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

}
