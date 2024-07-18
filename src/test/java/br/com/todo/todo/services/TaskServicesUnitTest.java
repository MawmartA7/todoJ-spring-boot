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
public class TaskServicesUnitTest {

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
                @DisplayName("Should be return a Task List existent")
                void shouldBeReturnedATaskListExistent() {
                        when(repository.findAll()).thenReturn(tasks);

                        List<TaskDTO> taskDTOListReturned = taskServices.getAllTasks();

                        assertNotNull(taskDTOListReturned, "The TaskDTO list returned is null");

                        assertEquals(tasksDTO.get(1), taskDTOListReturned.get(0),
                                        "The index 0 in the GetAllTasksTests not is the TaskDTO expected");
                        assertEquals(tasksDTO.get(3), taskDTOListReturned.get(1),
                                        "The index 1 in the GetAllTasksTests not is the TaskDTO expected");
                        assertEquals(tasksDTO.get(0), taskDTOListReturned.get(2),
                                        "The index 2 in the GetAllTasksTests not is the TaskDTO expected");
                        assertEquals(tasksDTO.get(2), taskDTOListReturned.get(3),
                                        "The index 3 in the GetAllTasksTests not is the TaskDTO expected");
                        assertEquals(tasksDTO.size(), taskDTOListReturned.size(),
                                        "The number of TaskDTO within the returned list is not the expected number");

                        verify(repository, times(1)).findAll();
                }

                @Test
                @DisplayName("Should be return a empty Task list")
                void shouldReturnedAEmptyTaskList() {
                        when(repository.findAll()).thenReturn(List.of());

                        List<TaskDTO> taskListReturned = taskServices.getAllTasks();

                        assertNotNull(taskListReturned, "The TaskDTO list returned is null");

                        assertEquals(List.of().size(), taskListReturned.size(), "The return was not an empty list");

                        verify(repository, times(1)).findAll();
                }
        }

        @Nested
        public class GetTaskByIdTests {

                @Test
                @DisplayName("Should be return a existent Task by id")
                void shouldReturnedAExistentTaskById() {
                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(task));

                        TaskDTO taskDTOReturned = taskServices.getTaskById(task.getId());

                        assertNotNull(taskDTOReturned, "The TaskDTO returned is null");

                        assertEquals(idCArgumentCaptor.getValue(), taskDTOReturned.id(),
                                        "The id of the returned TaskDTO is not the same as the id of the expected Task");
                        assertEquals(taskDTO, taskDTOReturned,
                                        "The TaskDTO returned is not equal to the expected TaskDTO");

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                }

                @Test
                @DisplayName("It should throw a NotFoundException exception when the Task is not found")
                void shouldThrowNotFoundExceptionWhenTaskNotFound() {
                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.empty());

                        NotFoundException exceptionReturned = assertThrows(NotFoundException.class,
                                        () -> taskServices.getTaskById(1L),
                                        "The NotFoundException exception is not thrown when the Task is not found");

                        assertEquals("Task not found", exceptionReturned.getMessage(),
                                        "The message of the NotFoundException is not equals message expected");
                        assertEquals("It was not possible to find a task with the specified id, try another one.",
                                        exceptionReturned.getDetails(),
                                        "The description of the NotFoundException is not equals description expected");

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                }
        }

        @Nested
        public class getAllDoneTasksTests {

                @Test
                @DisplayName("Should be return a Task list with done tasks")
                void shouldReturnedADoneTasksList() {
                        when(repository.findByDoneTrue()).thenReturn(Arrays.asList(task2, task3));

                        List<TaskDTO> taskDTOListReturned = taskServices.getAllDoneTasks();

                        assertNotNull(taskDTOListReturned, "The TaskDTO List returned is null");

                        assertEquals(tasksDTO.get(1), taskDTOListReturned.get(0),
                                        "The index 0 in the getAllDoneTasks not is the TaskDTO expected");
                        assertEquals(tasksDTO.get(2), taskDTOListReturned.get(1),
                                        "The index 1 in the getAllDoneTasks not is the TaskDTO expected");
                        assertEquals(tasksDTO.size() - 2, taskDTOListReturned.size(),
                                        "The number of TaskDTO within the returned list is not the expected number");

                        verify(repository, times(1)).findByDoneTrue();
                }

                @Test
                @DisplayName("Should be return a empty Task list with done tasks")
                void shouldReturnedAnEmptyDoneTasksList() {
                        when(repository.findByDoneTrue()).thenReturn(Arrays.asList());

                        List<TaskDTO> taskListReturned = taskServices.getAllDoneTasks();

                        assertEquals(0, taskListReturned.size(), "The return was not an empty list");

                        verify(repository, times(1)).findByDoneTrue();
                }
        }

        @Nested
        public class getAllPendingTasksTests {

                @Test
                @DisplayName("Should be return a Task list with pending tasks")
                void shouldReturnedADoneTasksList() {
                        when(repository.findByDoneFalse()).thenReturn(Arrays.asList(task, task4));

                        List<TaskDTO> taskDTOListReturned = taskServices.getAllPendingTasks();

                        assertNotNull(taskDTOListReturned, "The TaskDTO List returned is null");

                        assertEquals(tasksDTO.get(3), taskDTOListReturned.get(0),
                                        "The index 0 in the GetAllPendingTasksTests not is the TaskDTO expected");
                        assertEquals(tasksDTO.get(0), taskDTOListReturned.get(1),
                                        "The index 1 in the GetAllPendingTasksTests not is the TaskDTO expected");
                        assertEquals(tasksDTO.size() - 2, taskDTOListReturned.size(),
                                        "The number of TaskDTO within the returned list is not the expected number");

                        verify(repository, times(1)).findByDoneFalse();
                }

                @Test
                @DisplayName("Should be return a empty Task list with pending tasks")
                void shouldReturnedAnEmptyDoneTasksList() {
                        when(repository.findByDoneFalse()).thenReturn(Arrays.asList());

                        List<TaskDTO> taskListReturned = taskServices.getAllPendingTasks();

                        assertEquals(0, taskListReturned.size(), "The return was not an empty list");

                        verify(repository, times(1)).findByDoneFalse();
                }
        }

        @Nested
        public class postCreateTask {

                @Test
                @DisplayName("Should be return a new Task created")
                void shouldReturnedANewTaskCreated() {
                        TaskDTO taskDTOWithoutId = new TaskDTO(null, "Task name", "Task description", 2, false);

                        when(repository.save(taskArgumentCaptor.capture())).thenReturn(task);

                        TaskDTO taskDTOReturned = taskServices.postCreateTask(taskDTOWithoutId);

                        assertNotNull(taskDTOReturned, "The TaskDTO returned is null");

                        assertTrue(taskDTOReturned instanceof TaskDTO, "The return is not a instance of TaskDTO");

                        assertEquals(task.getId(), taskDTOReturned.id(),
                                        "The id of the returned TaskDTO is not the same as the id of the expected Task");
                        assertEquals(task.getName(), taskArgumentCaptor.getValue().getName(),
                                        "The name of the Task before delivering the Task to repository as been modified");
                        assertEquals(task.getDescription(), taskArgumentCaptor.getValue().getDescription(),
                                        "The description of the Task before delivering the Task to repository as been modified");
                        assertEquals(task.getPriority(), taskArgumentCaptor.getValue().getPriority(),
                                        "The priority of the Task before delivering the Task to repository as been modified");
                        assertEquals(task.getDone(), taskArgumentCaptor.getValue().getDone(),
                                        "The done of the Task before delivering the Task to repository as been modified");
                        assertEquals(taskDTO, taskDTOReturned,
                                        "The TaskDTO returned is not equals a the expected Task");

                        verify(repository, times(1)).save(taskArgumentCaptor.getValue());
                }
        }

        @Nested
        class putUpdateTask {

                @Test
                @DisplayName("Should be return an updated Task")
                void shouldReturnedAnUpdatedTask() {
                        Task taskToUpdate = new Task(1L, "Task name to update", "Task description to update", 1, true);

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(taskToUpdate));
                        when(repository.save(taskArgumentCaptor.capture())).thenReturn(task);

                        TaskDTO taskDTOUpdatedReturned = taskServices.putUpdateTask(taskDTO, taskToUpdate.getId());

                        assertNotNull(taskDTOUpdatedReturned, "The TaskDTO returned is null");

                        assertTrue(taskDTOUpdatedReturned instanceof TaskDTO,
                                        "The return is not a instance of TaskDTO");

                        assertEquals(taskToUpdate.getId(), taskDTOUpdatedReturned.id(),
                                        "The returned TaskDTO has a different id from the id of the expected Task");
                        assertEquals(taskArgumentCaptor.getValue().getId(), taskDTO.id(),
                                        "The Task that is delivered to the repository has a different id from the id of the expected Task");
                        assertEquals(taskDTO, taskDTOUpdatedReturned,
                                        "The TaskDTO returned is not equal to the expected TaskDTO");

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(1)).save(taskArgumentCaptor.getValue());
                }

                @Test
                @DisplayName("It should throw a NotFoundException exception when the Task to be updated is not found")
                void shouldThrowNotFoundExceptionWhenTaskToUpdateNotFound() {
                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.empty());

                        NotFoundException exceptionReturned = assertThrows(NotFoundException.class,
                                        () -> taskServices.putUpdateTask(taskDTO, 1L),
                                        "The NotFoundException exception is not thrown when the Task is not found");

                        assertEquals("Task not found", exceptionReturned.getMessage(),
                                        "The message of the NotFoundException is not equals message expected");
                        assertEquals("It was not possible to find a task with the specified id, try another one.",
                                        exceptionReturned.getDetails(),
                                        "The description of the NotFoundException is not equals message expected");

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(0)).save(any());
                }
        }

        @Nested
        class patchPartialUpdateTask {
                Task taskUpdatedExpected;

                @BeforeEach
                void setUp() {
                        taskUpdatedExpected = new Task(1L, "Expected Task name", "Expected Task description", 2, true);
                }

                @Test
                @DisplayName("should return a Task with the all modified fields by the given TaskDTO")
                void shouldBeReturnATaskWithTheAllModifiedFields() {
                        Task taskToUpdate = new Task(1L, "Task name to update", "Task description to update", 3, false);
                        TaskDTO partialUpdate = new TaskDTO(null, "Expected Task name", "Expected Task description", 2,
                                        true);

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(taskToUpdate));
                        when(repository.save(taskArgumentCaptor.capture())).thenReturn(taskUpdatedExpected);

                        TaskDTO taskDTOReturned = taskServices.patchPartialUpdateTask(partialUpdate,
                                        taskToUpdate.getId());

                        assertNotNull(taskDTOReturned, "The TaskDTO returned is null");
                        assertEquals(taskUpdatedExpected, taskArgumentCaptor.getValue(),
                                        "The Task to delivering to repository is not equal to the expected Task");
                        assertEquals(new TaskDTO(taskUpdatedExpected), taskDTOReturned,
                                        "The TaskDTO returned is not equal to the expected TaskDTO");

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(1)).save(taskArgumentCaptor.getValue());
                }

                @Test
                @DisplayName("should return a Task with the name modified by the given name")
                void shouldBeReturnATaskWithTheModifiedName() {
                        Task taskToUpdate = new Task(1L, "Task name to update", "Expected Task description", 2, true);
                        TaskDTO partialUpdate = new TaskDTO(null, "Expected Task name", null, null, null);

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(taskToUpdate));
                        when(repository.save(taskArgumentCaptor.capture())).thenReturn(taskUpdatedExpected);

                        TaskDTO taskDTOReturned = taskServices.patchPartialUpdateTask(partialUpdate,
                                        taskToUpdate.getId());

                        assertNotNull(taskDTOReturned, "The TaskDTO returned is null");
                        assertEquals(taskUpdatedExpected, taskArgumentCaptor.getValue(),
                                        "The Task to delivering for the repository is not equal to the expected Task");
                        assertEquals(new TaskDTO(taskUpdatedExpected), taskDTOReturned,
                                        "The TaskDTO returned is not equal to the expected TaskDTO");

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(1)).save(taskArgumentCaptor.getValue());
                }

                @Test
                @DisplayName("should return a Task with the description modified by the given description")
                void shouldBeReturnATaskWithTheModifiedDescription() {
                        Task taskToUpdate = new Task(1L, "Expected Task name", "Task description to update", 2, true);
                        TaskDTO partialUpdate = new TaskDTO(null, null, "Expected Task description", null, null);

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(taskToUpdate));
                        when(repository.save(taskArgumentCaptor.capture())).thenReturn(taskUpdatedExpected);

                        TaskDTO taskDTOReturned = taskServices.patchPartialUpdateTask(partialUpdate,
                                        taskToUpdate.getId());

                        assertNotNull(taskDTOReturned, "The TaskDTO returned is null");
                        assertEquals(taskUpdatedExpected, taskArgumentCaptor.getValue(),
                                        "The Task to delivering for the repository is not equal to the expected Task");
                        assertEquals(new TaskDTO(taskUpdatedExpected), taskDTOReturned,
                                        "The TaskDTO returned is not equal to the expected TaskDTO");

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(1)).save(taskArgumentCaptor.getValue());
                }

                @Test
                @DisplayName("should return a Task with the priority modified by the given priority")
                void shouldBeReturnATaskWithTheModifiedPriority() {
                        Task taskToUpdate = new Task(1L, "Expected Task name", "Expected Task description", 4, true);
                        TaskDTO partialUpdate = new TaskDTO(null, null, null, 2, null);

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(taskToUpdate));
                        when(repository.save(taskArgumentCaptor.capture())).thenReturn(taskUpdatedExpected);

                        TaskDTO taskDTOReturned = taskServices.patchPartialUpdateTask(partialUpdate,
                                        taskToUpdate.getId());

                        assertNotNull(taskDTOReturned, "The TaskDTO returned is null");
                        assertEquals(taskUpdatedExpected, taskArgumentCaptor.getValue(),
                                        "The Task to delivering for the repository is not equal to the expected Task");
                        assertEquals(new TaskDTO(taskUpdatedExpected), taskDTOReturned,
                                        "The TaskDTO returned is not equal to the expected TaskDTO");

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(1)).save(taskArgumentCaptor.getValue());
                }

                @Test
                @DisplayName("should return a Task with the done modified by the given done")
                void shouldBeReturnATaskWithTheModifiedDone() {
                        Task taskToUpdate = new Task(1L, "Expected Task name", "Expected Task description", 2, false);
                        TaskDTO partialUpdate = new TaskDTO(null, null, null, null, true);

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(taskToUpdate));
                        when(repository.save(taskArgumentCaptor.capture())).thenReturn(taskUpdatedExpected);

                        TaskDTO taskDTOReturned = taskServices.patchPartialUpdateTask(partialUpdate,
                                        taskToUpdate.getId());

                        assertNotNull(taskDTOReturned, "The TaskDTO returned is null");
                        assertEquals(taskUpdatedExpected, taskArgumentCaptor.getValue(),
                                        "The Task to delivering to the repository is not equal to the expected Task");
                        assertEquals(new TaskDTO(taskUpdatedExpected), taskDTOReturned,
                                        "The TaskDTO returned is not equal to the expected TaskDTO");

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(1)).save(taskArgumentCaptor.getValue());
                }

                @Test
                @DisplayName("It should throw a NotFoundException exception when the Task to be partially updated is not found")
                void shouldThrowNotFoundExceptionWhenTaskToUpdateNotFound() {
                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.empty());

                        NotFoundException exceptionReturned = assertThrows(NotFoundException.class,
                                        () -> taskServices.patchPartialUpdateTask(taskDTO, 1L),
                                        "The NotFoundException exception is not thrown when the Task is not found");

                        assertEquals("Task not found", exceptionReturned.getMessage(),
                                        "The message of the NotFoundException is not equals message expected");
                        assertEquals("It was not possible to find a task with the specified id, try another one.",
                                        exceptionReturned.getDetails(),
                                        "The description of the NotFoundException is not equals message expected");

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(0)).save(any());
                }

                @Test
                @DisplayName("It should throw an IllegalArgumentException exception when at least one field is not provided to update the Task other than the id")
                void shouldThrowBadRequestExceptionWhenPriorityIsOutOfRange() {
                        TaskDTO taskDTOEmpty = new TaskDTO(null, null, null, null, null);

                        IllegalArgumentException exceptionReturned = assertThrows(IllegalArgumentException.class,
                                        () -> taskServices.patchPartialUpdateTask(taskDTOEmpty, 1L),
                                        "The IllegalArgumentException exception is not thrown when the all values of the TaskDTO are null");

                        assertEquals("At least one field must be provided to update the task",
                                        exceptionReturned.getMessage(),
                                        "The message of the IllegalArgumentException is not equals message expected");

                        verify(repository, times(0)).findById(any());
                        verify(repository, times(0)).save(any());
                }
        }

        @Nested
        public class deleteTask {
                @Test
                @DisplayName("Should delete a Task by id")
                void shouldDeleteATask() {
                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(task));

                        taskServices.deleteTask(task.getId());

                        assertEquals(task.getId(), idCArgumentCaptor.getValue(),
                                        "The id passed in the parameter is not the same id of the expected Task");

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(1)).deleteById(idCArgumentCaptor.getValue());
                }

                @Test
                @DisplayName("It should throw a NotFoundException exception when the Task to be deleted is not found")
                void shouldThrowNotFoundExceptionWhenTaskToDeleteNotFound() {
                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.empty());

                        NotFoundException exceptionReturned = assertThrows(NotFoundException.class,
                                        () -> taskServices.deleteTask(1L),
                                        "The NotFoundException exception is not thrown when the Task is not found");

                        assertEquals("Task not found", exceptionReturned.getMessage(),
                                        "The message of the NotFoundException is not equals message expected");
                        assertEquals("It was not possible to find a task with the specified id, try another one.",
                                        exceptionReturned.getDetails(),
                                        "The description of the NotFoundException is not equals message expected");

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(0)).deleteById(idCArgumentCaptor.getValue());
                }
        }
}
