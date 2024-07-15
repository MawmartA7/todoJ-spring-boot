package br.com.todo.todo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

    @Captor
    private ArgumentCaptor<Task> taskArgumentCaptor;

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

            TaskDTO taskDTOReturned = taskServices.getTaskById(task.getId());

            assertEquals(taskDTO, taskDTOReturned);
            assertEquals(idCArgumentCaptor.getValue(), taskDTOReturned.id());

            verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
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

            verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
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

    @Nested
    public class postCreateTask {

        @Test
        @DisplayName("Should be return a new task created")
        void shouldReturnedANewTaskCreated() {
            TaskDTO taskDTOWithoutId = new TaskDTO(null, "Task name", "Task description", 2, false);

            when(repository.save(taskArgumentCaptor.capture())).thenReturn(task);

            TaskDTO taskDTOReturned = taskServices.postCreateTask(taskDTOWithoutId);

            assertNotNull(taskDTOReturned);
            assertTrue(taskDTOReturned instanceof TaskDTO);
            assertEquals(task.getId(), taskDTOReturned.id());
            assertEquals(task.getName(), taskArgumentCaptor.getValue().getName());
            assertEquals(task.getDescription(), taskArgumentCaptor.getValue().getDescription());
            assertEquals(task.getPriority(), taskArgumentCaptor.getValue().getPriority());
            assertEquals(task.getDone(), taskArgumentCaptor.getValue().getDone());
            assertEquals(taskDTO, taskDTOReturned);

            verify(repository, times(1)).save(taskArgumentCaptor.getValue());
        }
    }

    @Nested
    class putUpdateTask {

        @Test
        @DisplayName("Should be return an updated task")
        void shouldReturnedAnUpdatedTask() {
            Task taskToUpdate = new Task(1L, "Task name to update", "Task description to update", 1, true);

            when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(taskToUpdate));
            when(repository.save(taskArgumentCaptor.capture())).thenReturn(task);

            TaskDTO taskDTOUpdatedReturned = taskServices.putUpdateTask(taskDTO, taskToUpdate.getId());

            assertNotNull(taskDTOUpdatedReturned);
            assertTrue(taskDTOUpdatedReturned instanceof TaskDTO);
            assertEquals(taskDTO, taskDTOUpdatedReturned);
            assertTrue(taskToUpdate.getId() == taskDTO.id() && taskArgumentCaptor.getValue().getId() == taskDTO.id());

            verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
            verify(repository, times(1)).save(taskArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("It should throw a NotFoundException exception when the task to be updated is not found")
        void shouldThrowNotFoundExceptionWhenTaskToUpdateNotFound() {
            when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.empty());
            NotFoundException exceptionReturned = assertThrows(NotFoundException.class,
                    () -> taskServices.putUpdateTask(taskDTO, 1L));

            assertEquals("Task not found", exceptionReturned.getMessage());
            assertEquals("It was not possible to find a task with the specified id, try another one.",
                    exceptionReturned.getDetails());

            verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
            verify(repository, times(0)).save(any());
        }
    }

    @Nested
    class patchPartialUpdateTask {
        @Test
        @DisplayName("Should be return a partially updated task")
        void shouldReturnedAPartiallyUpdatedTask() {
            Task taskUpdatedExpected = new Task(1L, "Task name updated", "Task description not updated ", 2,
                    true);
            Task taskToUpdate = new Task(1L, "Task name to update", "Task description not updated", 3, true);
            TaskDTO partialUpdate = new TaskDTO(null, "Task name updated", null, 2, null);

            when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(taskToUpdate));
            when(repository.save(taskArgumentCaptor.capture())).thenReturn(taskUpdatedExpected);

            TaskDTO taskDTOPartiallyUpdatedReturned = taskServices.patchPartialUpdateTask(partialUpdate,
                    taskToUpdate.getId());

            assertNotNull(taskDTOPartiallyUpdatedReturned);
            assertTrue(taskDTOPartiallyUpdatedReturned instanceof TaskDTO);
            assertTrue(taskToUpdate.getId() == taskUpdatedExpected.getId()
                    && taskArgumentCaptor.getValue().getId() == taskUpdatedExpected.getId());

            assertEquals(taskArgumentCaptor.getValue().getName(), taskDTOPartiallyUpdatedReturned.name());

            assertEquals(taskUpdatedExpected.getName(), taskDTOPartiallyUpdatedReturned.name());
            assertEquals(taskUpdatedExpected.getDescription(), taskDTOPartiallyUpdatedReturned.description());
            assertEquals(taskUpdatedExpected.getPriority(), taskDTOPartiallyUpdatedReturned.priority());
            assertEquals(taskUpdatedExpected.getDone(), taskDTOPartiallyUpdatedReturned.done());

            verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
            verify(repository, times(1)).save(taskArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("It should not update the task when the given fields are null")
        void shouldNotUpdateTaskWhenFieldsAreNull() {

        }

        @Test
        @DisplayName("It should throw a NotFoundException exception when the task to be partially updated is not found")
        void shouldThrowNotFoundExceptionWhenTaskToUpdateNotFound() {
            when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.empty());

            NotFoundException exceptionReturned = assertThrows(NotFoundException.class,
                    () -> taskServices.patchPartialUpdateTask(taskDTO, 1L));

            assertEquals("Task not found", exceptionReturned.getMessage());
            assertEquals("It was not possible to find a task with the specified id, try another one.",
                    exceptionReturned.getDetails());

            verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
            verify(repository, times(0)).save(any());
        }

        @Test
        @DisplayName("It should throw an IllegalArgumentException exception when at least one field is not provided to update the task other than the id")
        void shouldThrowBadRequestExceptionWhenPriorityIsOutOfRange() {
            TaskDTO taskDTOEmpty = new TaskDTO(null, null, null, null, null);

            IllegalArgumentException exceptionReturned = assertThrows(IllegalArgumentException.class,
                    () -> taskServices.patchPartialUpdateTask(taskDTOEmpty, 1L));

            assertEquals("At least one field must be provided to update the task", exceptionReturned.getMessage());

            verify(repository, times(0)).findById(any());
            verify(repository, times(0)).save(any());
        }
    }

    @Nested
    public class deleteTask {
        @Test
        @DisplayName("Should delete a task by id")
        void shouldDeleteATask() {
            when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(task));

            taskServices.deleteTask(task.getId());

            assertEquals(task.getId(), idCArgumentCaptor.getValue());

            verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
            verify(repository, times(1)).deleteById(idCArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("It should throw a NotFoundException exception when the task to be deleted is not found")
        void shouldThrowNotFoundExceptionWhenTaskToDeleteNotFound() {
            when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.empty());

            NotFoundException exceptionReturned = assertThrows(NotFoundException.class,
                    () -> taskServices.deleteTask(1L));

            assertEquals("Task not found", exceptionReturned.getMessage());
            assertEquals("It was not possible to find a task with the specified id, try another one.",
                    exceptionReturned.getDetails());
            assertEquals(1L, idCArgumentCaptor.getValue());

            verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
            verify(repository, times(0)).deleteById(idCArgumentCaptor.getValue());
        }
    }
}
