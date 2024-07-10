package br.com.todo.todo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.todo.todo.dto.TaskDTO;
import br.com.todo.todo.models.Task;
import br.com.todo.todo.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServicesTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskServices taskServices;

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
}
