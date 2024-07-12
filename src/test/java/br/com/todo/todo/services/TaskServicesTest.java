package br.com.todo.todo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.todo.todo.dto.TaskDTO;
import br.com.todo.todo.exceptions.NotFoundException;
import br.com.todo.todo.models.Task;
import br.com.todo.todo.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServicesTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskServices taskServices;

    @Captor
    private ArgumentCaptor<Long> idCArgumentCaptor;
    private TaskDTO taskDTO;
    private List<TaskDTO> tasksDTO;
    private Task task;
    private List<Task> tasks;

    @BeforeEach
    public void setup() {
        taskDTO = new TaskDTO(1L, "Task name", "Task description", 1, false);
        tasksDTO = Arrays.asList(taskDTO);
        task = new Task(1L, "Task name", "Task description", 1, false);
        tasks = Arrays.asList(task);
    }

    @Nested
    public class GetAllTaskTests {

        @Test
        @DisplayName("Should be return a task List existent")
        void shouldBeReturnedATaskListExistent() {
            when(repository.findAll()).thenReturn(tasks);
            List<TaskDTO> taskDTOListReturned = taskServices.getAllTasks();
            assertEquals(tasksDTO.get(0), taskDTOListReturned.get(0));
            assertEquals(tasksDTO.size(), taskDTOListReturned.size());
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should be return a empty task list")
        void shouldReturnedAEmptyTaskList() {
            when(repository.findAll()).thenReturn(List.of());
            List<TaskDTO> taskListReturned = taskServices.getAllTasks();
            assertEquals(List.of().size(), taskListReturned.size());
            verify(repository, times(1)).findAll();
        }
    }

    @Nested
    public class GetTaskByIdTests {

        @Test
        @DisplayName("Should be return a existent task by id")
        void shouldReturnedAExistentTaskById() {
            when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(task));

            TaskDTO taskDTOReturned = taskServices.getTaskById(1L);

            assertEquals(taskDTO, taskDTOReturned);
            assertEquals(idCArgumentCaptor.getValue(), taskDTOReturned.id());

            verify(repository, times(1)).findById(idCArgumentCaptor.capture());
        }

        @Test
        @DisplayName("It should throw a NotFoundException exception when the task is not found")
        void shouldThrowNotFoundExceptionWhenTaskNotFound() {
            when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.empty());

            NotFoundException exceptionReturned = assertThrows(NotFoundException.class,
                    () -> taskServices.getTaskById(1L));

            assertEquals("Task not found", exceptionReturned.getMessage());
            assertEquals("It was not possible to find a task with the specified id, try another one.",
                    exceptionReturned.getDetails());

            verify(repository, times(1)).findById(idCArgumentCaptor.capture());
        }
    }

    @Nested
    public class getAllDoneTasks {

        @Test
        @DisplayName("Should be return a task list with done tasks")
        void shouldReturnedADoneTasksList() {
            tasks.get(0).setDone(false);
            when(repository.findByDoneTrue()).thenReturn(tasks);
            List<TaskDTO> taskDTOListReturned = taskServices.getAllDoneTasks();
            assertEquals(tasksDTO.get(0), taskDTOListReturned.get(0));
            assertEquals(tasksDTO.size(), taskDTOListReturned.size());
            verify(repository, times(1)).findByDoneTrue();
        }

        @Test
        @DisplayName("Should be return a empty task list with done tasks")
        void shouldReturnedAnEmptyDoneTasksList() {
            when(repository.findByDoneTrue()).thenReturn(Arrays.asList());
            List<TaskDTO> taskListReturned = taskServices.getAllDoneTasks();
            assertEquals(List.of().size(), taskListReturned.size());
            verify(repository, times(1)).findByDoneTrue();
        }
    }
}
