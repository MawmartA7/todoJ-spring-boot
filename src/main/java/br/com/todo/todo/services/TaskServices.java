package br.com.todo.todo.services;

import br.com.todo.todo.dto.TaskDTO;
import br.com.todo.todo.exceptions.NotFoundException;
import br.com.todo.todo.models.Task;
import br.com.todo.todo.repository.TaskRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServices {

    private final TaskRepository taskRepository;

    public TaskServices(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDTO> getAllTasks() {
        Sort sort = Sort.by(Sort.Direction.DESC, "priority").and(Sort.by(Sort.Direction.ASC, "id"));
        List<Task> taskList = taskRepository.findAll(sort);
        return taskList.stream().map(TaskDTO::new).collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new NotFoundException("Task not found",
                        "It was not possible to find a task with the specified id, try another one."));
        return new TaskDTO(task);
    }

    public ResponseEntity<?> getAllDoneTasks() {
        return new ResponseEntity<>(taskRepository.findByDoneTrue(), HttpStatus.OK);
    }

    public ResponseEntity<?> getAllPendingTasks() {
        return new ResponseEntity<>(taskRepository.findByDoneFalse(), HttpStatus.OK);
    }

    public TaskDTO postCreateTask(TaskDTO taskDTO) {
        return new TaskDTO(taskRepository.save(new Task(taskDTO)));
    }

    public TaskDTO putUpdateTask(TaskDTO taskDTO, Long taskId) {
        Optional<Task> existingTask = taskRepository.findById(taskId);
        if (existingTask.isPresent()) {
            Task updatedTask = new Task(taskDTO, taskId);
            return new TaskDTO(taskRepository.save(updatedTask));
        } else {
            throw new NotFoundException("Task not found",
                    "It was not possible to find a task with the specified id, try another one.");
        }
    }

    public TaskDTO patchPartialUpdateTask(TaskDTO taskDTO, Long taskId) {
        if (taskDTO.name() == null && taskDTO.description() == null && taskDTO.priority() == null
                && taskDTO.done() == null) {
            throw new IllegalArgumentException("At least one field must be provided to update the task");
        }
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
            return new TaskDTO(taskRepository.save(taskToUpdate));
        } else {
            throw new NotFoundException("Task not found",
                    "It was not possible to find a task with the specified id, try another one.");
        }
    }

    public void deleteTask(Long taskId) {
        Optional<Task> existingTask = taskRepository.findById(taskId);
        if (existingTask.isPresent()) {
            taskRepository.deleteById(taskId);
        } else {
            throw new NotFoundException("Task not found",
                    "It was not possible to find a task with the specified id, try another one.");
        }
    }
}
