package br.com.todo.todo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.todo.todo.repository.TaskRepository;

@Service
public class TaskServices {
    @Autowired
    private TaskRepository taskRepository;

    public ResponseEntity<?> getAllTasks() {
        return new ResponseEntity<>(taskRepository.findAll(), HttpStatus.OK);
    }
}
