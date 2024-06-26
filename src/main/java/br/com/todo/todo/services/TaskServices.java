package br.com.todo.todo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.todo.todo.dto.TaskDTO;
import br.com.todo.todo.models.Task;
import br.com.todo.todo.repository.TaskRepository;
import jakarta.validation.Valid;

@Service
public class TaskServices {
    @Autowired
    private TaskRepository taskRepository;

    public ResponseEntity<?> getAllTasks() {
        return new ResponseEntity<>(taskRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<?> postCreateTask(@Valid TaskDTO taskDTO) {
        Task newTask = taskRepository.save(new Task(taskDTO));
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    public ResponseEntity<?> putUpdateTask(TaskDTO taskDTO, Long taskId) {
        Optional<Task> existingTask = taskRepository.findById(taskId);
        if (existingTask.isPresent()) {
            Task updatedTask = new Task(taskDTO, taskId);
            taskRepository.save(updatedTask);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("task not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> patchPartialUpdateTask(TaskDTO taskDTO, Long taskId) {
        Optional<Task> existingTask = taskRepository.findById(taskId);
        if (existingTask.isPresent()) {
            Task taskToUpdate = existingTask.get();
            if (taskDTO.name() != null && !(taskDTO.name().equals(taskToUpdate.getName())))
                taskToUpdate.setName(taskDTO.name());
            if (taskDTO.description() != null && !(taskDTO.description().equals(taskToUpdate.getDescription())))
                taskToUpdate.setDescription(taskDTO.description());
            if (taskDTO.priority() != null && !(taskDTO.priority().equals(taskToUpdate.getPriority())))
                taskToUpdate.setPriority(taskDTO.priority());
            if (taskDTO.done() != null && !(taskDTO.done().equals(taskToUpdate.getDone())))
                taskToUpdate.setDone(taskDTO.done());

            return new ResponseEntity<>(taskRepository.save(taskToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("task not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> deleteTask(Long taskId) {
        Optional<Task> existingTask = taskRepository.findById(taskId);
        if (existingTask.isPresent()) {
            taskRepository.deleteById(taskId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("task not found", HttpStatus.NOT_FOUND);
        }
    }
}
