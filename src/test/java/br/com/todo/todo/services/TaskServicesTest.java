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
    private TaskDTO taskDTO2;
    private TaskDTO taskDTO3;
    private TaskDTO taskDTO4;
    private List<TaskDTO> tasksDTO;
    private Task task;
    private Task task2;
    private Task task3;
    private Task task4;
    private List<Task> tasks;

    @BeforeEach
    public void setup() {
        taskDTO = new TaskDTO(1L, "Task name", "Task description", 2, false);
        taskDTO2 = new TaskDTO(2L, "Other task name", "Other task description", 1, true);
        taskDTO3 = new TaskDTO(3L, "Other task name", "Other task description", 3, true);
        taskDTO4 = new TaskDTO(4L, "Other task name", "Other task description", 1, false);
        tasksDTO = Arrays.asList(taskDTO, taskDTO2, taskDTO3, taskDTO4);
        task = new Task(1L, "Task name", "Task description", 2, false);
        task2 = new Task(2L, "Other task name", "Other task description", 1, true);
        task3 = new Task(3L, "Other task name", "Other task description", 3, true);
        task4 = new Task(4L, "Other task name", "Other task description", 1, false);
        tasks = Arrays.asList(task, task2, task3, task4);
    }

    @Nested
    public class GetAllTasksTests {

        @Test
        @DisplayName("Should be return a task List existent")
        void shouldBeReturnedATaskListExistent() {
            when(repository.findAll()).thenReturn(tasks);
            List<TaskDTO> taskDTOListReturned = taskServices.getAllTasks();
            assertEquals(tasksDTO.get(1), taskDTOListReturned.get(0));
            assertEquals(tasksDTO.get(3), taskDTOListReturned.get(1));
            assertEquals(tasksDTO.get(0), taskDTOListReturned.get(2));
            assertEquals(tasksDTO.get(2), taskDTOListReturned.get(3));
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
            when(repository.findByDoneTrue()).thenReturn(Arrays.asList(task2, task3));
            List<TaskDTO> taskDTOListReturned = taskServices.getAllDoneTasks();
            assertEquals(tasksDTO.get(1), taskDTOListReturned.get(0));
            assertEquals(tasksDTO.get(2), taskDTOListReturned.get(1));
            assertEquals(tasksDTO.size() - 2, taskDTOListReturned.size());
            verify(repository, times(1)).findByDoneTrue();
        }

        @Test
        @DisplayName("Should be return a empty task list with done tasks")
        void shouldReturnedAnEmptyDoneTasksList() {
            when(repository.findByDoneTrue()).thenReturn(Arrays.asList());
            List<TaskDTO> taskListReturned = taskServices.getAllDoneTasks();
            assertEquals(0, taskListReturned.size());
            verify(repository, times(1)).findByDoneTrue();
        }
    }

    @Nested
    public class getAllPendingTasks {

        @Test
        @DisplayName("Should be return a task list with pending tasks")
        void shouldReturnedADoneTasksList() {
            when(repository.findByDoneFalse()).thenReturn(Arrays.asList(task, task4));
            List<TaskDTO> taskDTOListReturned = taskServices.getAllPendingTasks();
            assertEquals(tasksDTO.get(3), taskDTOListReturned.get(0));
            assertEquals(tasksDTO.get(0), taskDTOListReturned.get(1));
            assertEquals(tasksDTO.size() - 2, taskDTOListReturned.size());
            verify(repository, times(1)).findByDoneFalse();
        }

        @Test
        @DisplayName("Should be return a empty task list with pending tasks")
        void shouldReturnedAnEmptyDoneTasksList() {
            when(repository.findByDoneFalse()).thenReturn(Arrays.asList());
            List<TaskDTO> taskListReturned = taskServices.getAllPendingTasks();
            assertEquals(0, taskListReturned.size());
            verify(repository, times(1)).findByDoneFalse();
        }
    }
}
