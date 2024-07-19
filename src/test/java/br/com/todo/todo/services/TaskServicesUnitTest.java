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
                task = new Task(taskDTO, taskDTO.id());
                task2 = new Task(taskDTO2, taskDTO2.id());
                task3 = new Task(taskDTO3, taskDTO3.id());
                task4 = new Task(taskDTO4, taskDTO4.id());
                tasks = Arrays.asList(task, task2, task3, task4);
        }

        @Nested
        public class GetAllTasksTests {

                @Test
                @DisplayName("Should return a list of TaskDTOs when tasks exist")
                void whenHaveTasks_thenReturnATaskDTOList() {
                        // Arrange

                        when(repository.findAll()).thenReturn(tasks);

                        // Act

                        List<TaskDTO> taskDTOListReturned = taskServices.getAllTasks();

                        // Assert

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

                        // verify

                        verify(repository, times(1)).findAll();

                }

                @Test
                @DisplayName("Should return an empty list when no tasks exist")
                void whenNoHaveTasks_thenReturnAEmptyList() {
                        // Arrange

                        when(repository.findAll()).thenReturn(List.of());

                        // Act

                        List<TaskDTO> taskListReturned = taskServices.getAllTasks();

                        // Assert

                        assertNotNull(taskListReturned, "The TaskDTO list returned is null");

                        assertEquals(List.of().size(), taskListReturned.size(), "The return was not an empty list");

                        // Verify

                        verify(repository, times(1)).findAll();

                }

        }

        @Nested
        public class GetTaskByIdTests {

                @Test
                @DisplayName("Should return TaskDTO when task exists by id")
                void whenHaveTaskFound_thenReturnTheTaskDTO() {
                        // Arrange

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(task));

                        // Act

                        TaskDTO taskDTOReturned = taskServices.getTaskById(task.getId());

                        // Assert

                        assertNotNull(taskDTOReturned, "The TaskDTO returned is null");

                        assertEquals(idCArgumentCaptor.getValue(), taskDTOReturned.id(),
                                        "The id of the returned TaskDTO is not the same as the id of the expected Task");
                        assertEquals(taskDTO, taskDTOReturned,
                                        "The TaskDTO returned is not equal to the expected TaskDTO");

                        // Verify

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());

                }

                @Test
                @DisplayName("Should throw NotFoundException when task not found by id")
                void whenTaskFound_thenThrowANotFoundException() {
                        // Arrange

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.empty());

                        // Act

                        NotFoundException exceptionReturned = assertThrows(NotFoundException.class,
                                        () -> taskServices.getTaskById(1L),
                                        "The NotFoundException exception is not thrown when the Task is not found");

                        // Assert

                        assertEquals("Task not found", exceptionReturned.getMessage(),
                                        "The message of the NotFoundException is not equals message expected");
                        assertEquals("It was not possible to find a task with the specified id, try another one.",
                                        exceptionReturned.getDetails(),
                                        "The description of the NotFoundException is not equals description expected");

                        // Verify

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());

                }

        }

        @Nested
        public class getAllDoneTasksTests {

                @Test
                @DisplayName("Should return a list of TaskDTOs with done true when such tasks exist")
                void whenHaveTasksWithDoneTrue_thenReturnATaskDTOListWithDoneTrue() {
                        // Arrange

                        when(repository.findByDoneTrue()).thenReturn(Arrays.asList(task2, task3));

                        // Act

                        List<TaskDTO> taskDTOListReturned = taskServices.getAllDoneTasks();

                        // Assert

                        assertNotNull(taskDTOListReturned, "The TaskDTO List returned is null");

                        assertEquals(tasksDTO.get(1), taskDTOListReturned.get(0),
                                        "The index 0 in the getAllDoneTasks not is the TaskDTO expected");
                        assertEquals(tasksDTO.get(2), taskDTOListReturned.get(1),
                                        "The index 1 in the getAllDoneTasks not is the TaskDTO expected");
                        assertEquals(tasksDTO.size() - 2, taskDTOListReturned.size(),
                                        "The number of TaskDTO within the returned list is not the expected number");

                        // Verify

                        verify(repository, times(1)).findByDoneTrue();

                }

                @Test
                @DisplayName("Should return an empty list when no tasks with done true exist")
                void whenNoHaveTasksWithDoneTrue_thenReturnAEmptyList() {
                        // Arrange

                        when(repository.findByDoneTrue()).thenReturn(Arrays.asList());

                        // Act

                        List<TaskDTO> taskListReturned = taskServices.getAllDoneTasks();

                        // Assert

                        assertEquals(0, taskListReturned.size(), "The return was not an empty list");

                        // Verify

                        verify(repository, times(1)).findByDoneTrue();

                }

        }

        @Nested
        public class getAllPendingTasksTests {

                @Test
                @DisplayName("Should return a list of TaskDTOs with done false when such tasks exist")
                void whenHaveTasksWithDoneFalse_thenReturnATaskListWithDoneFalse() {
                        // Arrange

                        when(repository.findByDoneFalse()).thenReturn(Arrays.asList(task, task4));

                        // Act

                        List<TaskDTO> taskDTOListReturned = taskServices.getAllPendingTasks();

                        // Assert

                        assertNotNull(taskDTOListReturned, "The TaskDTO List returned is null");

                        assertEquals(tasksDTO.get(3), taskDTOListReturned.get(0),
                                        "The index 0 in the GetAllPendingTasksTests not is the TaskDTO expected");
                        assertEquals(tasksDTO.get(0), taskDTOListReturned.get(1),
                                        "The index 1 in the GetAllPendingTasksTests not is the TaskDTO expected");
                        assertEquals(tasksDTO.size() - 2, taskDTOListReturned.size(),
                                        "The number of TaskDTO within the returned list is not the expected number");

                        // Verify

                        verify(repository, times(1)).findByDoneFalse();

                }

                @Test
                @DisplayName("Should return an empty list when no tasks with done false exist")
                void whenNoHaveTasksWithDoneFalse_thenReturnAEmptyList() {
                        // Arrange

                        when(repository.findByDoneFalse()).thenReturn(Arrays.asList());

                        // Act

                        List<TaskDTO> taskListReturned = taskServices.getAllPendingTasks();

                        // Assert

                        assertEquals(0, taskListReturned.size(), "The return was not an empty list");

                        // Verify

                        verify(repository, times(1)).findByDoneFalse();

                }

        }

        @Nested
        public class postCreateTask {

                @Test
                @DisplayName("Should return created TaskDTO when a task is created")
                void whenCreateTask_thenReturnTheTaskCreatedAsATaskDTO() {
                        // Arrange

                        TaskDTO taskDTOWithoutId = new TaskDTO(null, "Task name", "Task description", 2, false);

                        when(repository.save(taskArgumentCaptor.capture())).thenReturn(task);

                        // Act

                        TaskDTO taskDTOReturned = taskServices.postCreateTask(taskDTOWithoutId);

                        // Assert

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

                        // Verify

                        verify(repository, times(1)).save(taskArgumentCaptor.getValue());

                }

        }

        @Nested
        class putUpdateTask {

                TaskDTO taskDTOToUpdate;

                @BeforeEach
                void setup() {
                        taskDTOToUpdate = new TaskDTO(1L, "Task name to update", "Task description to update", 1, true);
                }

                @Test
                @DisplayName("Should return updated TaskDTO and update when the task exists")
                void whenUpdateTask_thenReturnTheTaskUpdatedAsATaskDTO() {
                        // Arrange

                        Task taskToUpdate = new Task(taskDTOToUpdate, taskDTOToUpdate.id());

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(taskToUpdate));
                        when(repository.save(taskArgumentCaptor.capture())).thenReturn(task);

                        // Act

                        TaskDTO taskDTOUpdatedReturned = taskServices.putUpdateTask(taskDTO, taskToUpdate.getId());

                        // Assert

                        assertNotNull(taskDTOUpdatedReturned, "The TaskDTO returned is null");

                        assertTrue(taskDTOUpdatedReturned instanceof TaskDTO,
                                        "The return is not a instance of TaskDTO");

                        assertEquals(taskToUpdate.getId(), taskDTOUpdatedReturned.id(),
                                        "The returned TaskDTO has a different id from the id of the expected Task");
                        assertEquals(taskArgumentCaptor.getValue().getId(), taskDTO.id(),
                                        "The Task that is delivered to the repository has a different id from the id of the expected Task");
                        assertEquals(taskDTO, taskDTOUpdatedReturned,
                                        "The TaskDTO returned is not equal to the expected TaskDTO");

                        // Verify

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(1)).save(taskArgumentCaptor.getValue());

                }

                @Test
                @DisplayName("Should throw NotFoundException when the task to be updated is not found")
                void whenTaskNotFound_thenThrowANotFoundException() {
                        // Arrange

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.empty());

                        // Act

                        NotFoundException exceptionReturned = assertThrows(NotFoundException.class,
                                        () -> taskServices.putUpdateTask(taskDTO, 1L),
                                        "The NotFoundException exception is not thrown when the Task is not found");

                        // Assert

                        assertEquals("Task not found", exceptionReturned.getMessage(),
                                        "The message of the NotFoundException is not equals message expected");
                        assertEquals("It was not possible to find a task with the specified id, try another one.",
                                        exceptionReturned.getDetails(),
                                        "The description of the NotFoundException is not equals message expected");

                        // Verify

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(0)).save(any());

                }

        }

        @Nested
        class patchPartialUpdateTask {

                TaskDTO taskDTOToUpdate;
                TaskDTO taskDTOUpdateExpected;
                Task taskUpdatedExpected;

                @BeforeEach
                void setUp() {
                        taskDTOToUpdate = new TaskDTO(1L, "Task name to update", "Task description to update", 3,
                                        false);
                        taskDTOUpdateExpected = new TaskDTO(1L, "Expected Task name", "Expected Task description", 2,
                                        true);
                        taskUpdatedExpected = new Task(taskDTOUpdateExpected, taskDTOUpdateExpected.id());
                }

                @Test
                @DisplayName("Should return updated TaskDTO when all fields are updated")
                void whenPartialUpdateHaveAllFieldsFill_thenReturnTheTaskUpdatedWithAllFieldsUpdatedAsATaskDTO() {
                        // Arrange

                        Task taskToUpdate = new Task(taskDTOToUpdate, taskDTOToUpdate.id());
                        TaskDTO partialUpdate = new TaskDTO(null, "Expected Task name", "Expected Task description", 2,
                                        true);

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(taskToUpdate));
                        when(repository.save(taskArgumentCaptor.capture())).thenReturn(taskUpdatedExpected);

                        // Act

                        TaskDTO taskDTOReturned = taskServices.patchPartialUpdateTask(partialUpdate,
                                        taskToUpdate.getId());

                        // Assert

                        assertNotNull(taskDTOReturned, "The TaskDTO returned is null");
                        assertEquals(taskUpdatedExpected, taskArgumentCaptor.getValue(),
                                        "The Task to delivering to repository is not equal to the expected Task");
                        assertEquals(new TaskDTO(taskUpdatedExpected), taskDTOReturned,
                                        "The TaskDTO returned is not equal to the expected TaskDTO");

                        // Verify

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(1)).save(taskArgumentCaptor.getValue());

                }

                @Test
                @DisplayName("Should return TaskDTO with updated name when name is updated")
                void whenPartialUpdateHaveNameFieldFill_thenReturnTheTaskUpdatedWithNameFieldUpdatedAsATaskDTO() {
                        // Arrange

                        Task taskToUpdate = new Task(taskDTOUpdateExpected, 1L);
                        taskToUpdate.setName("Task name to update");

                        TaskDTO partialUpdate = new TaskDTO(null, "Expected Task name", null, null, null);

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(taskToUpdate));
                        when(repository.save(taskArgumentCaptor.capture())).thenReturn(taskUpdatedExpected);

                        // Act

                        TaskDTO taskDTOReturned = taskServices.patchPartialUpdateTask(partialUpdate,
                                        taskToUpdate.getId());

                        // Assert

                        assertNotNull(taskDTOReturned, "The TaskDTO returned is null");
                        assertEquals(taskUpdatedExpected, taskArgumentCaptor.getValue(),
                                        "The Task to delivering for the repository is not equal to the expected Task");
                        assertEquals(new TaskDTO(taskUpdatedExpected), taskDTOReturned,
                                        "The TaskDTO returned is not equal to the expected TaskDTO");

                        // Verify

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(1)).save(taskArgumentCaptor.getValue());

                }

                @Test
                @DisplayName("Should return TaskDTO with updated description when description is updated")
                void whenPartialUpdateHaveDescriptionFieldFill_thenReturnTheTaskUpdatedWithDescriptionFieldUpdatedAsATaskDTO() {
                        // Arrange

                        Task taskToUpdate = new Task(taskDTOUpdateExpected, taskDTOUpdateExpected.id());
                        taskToUpdate.setDescription("Task description to update");

                        TaskDTO partialUpdate = new TaskDTO(null, null, "Expected Task description", null, null);

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(taskToUpdate));
                        when(repository.save(taskArgumentCaptor.capture())).thenReturn(taskUpdatedExpected);

                        // Act

                        TaskDTO taskDTOReturned = taskServices.patchPartialUpdateTask(partialUpdate,
                                        taskToUpdate.getId());

                        // Assert

                        assertNotNull(taskDTOReturned, "The TaskDTO returned is null");
                        assertEquals(taskUpdatedExpected, taskArgumentCaptor.getValue(),
                                        "The Task to delivering for the repository is not equal to the expected Task");
                        assertEquals(new TaskDTO(taskUpdatedExpected), taskDTOReturned,
                                        "The TaskDTO returned is not equal to the expected TaskDTO");

                        // Verify

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(1)).save(taskArgumentCaptor.getValue());

                }

                @Test
                @DisplayName("Should return TaskDTO with updated priority when priority is updated")
                void whenPartialUpdateHavePriorityFieldFill_thenReturnTheTaskUpdatedWithPriorityFieldUpdatedAsATaskDTO() {
                        // Arrange

                        Task taskToUpdate = new Task(taskDTOUpdateExpected, taskDTOUpdateExpected.id());
                        taskToUpdate.setPriority(4);

                        TaskDTO partialUpdate = new TaskDTO(null, null, null, 2, null);

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(taskToUpdate));
                        when(repository.save(taskArgumentCaptor.capture())).thenReturn(taskUpdatedExpected);

                        // Act

                        TaskDTO taskDTOReturned = taskServices.patchPartialUpdateTask(partialUpdate,
                                        taskToUpdate.getId());

                        // Assert

                        assertNotNull(taskDTOReturned, "The TaskDTO returned is null");
                        assertEquals(taskUpdatedExpected, taskArgumentCaptor.getValue(),
                                        "The Task to delivering for the repository is not equal to the expected Task");
                        assertEquals(new TaskDTO(taskUpdatedExpected), taskDTOReturned,
                                        "The TaskDTO returned is not equal to the expected TaskDTO");

                        // Verify

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(1)).save(taskArgumentCaptor.getValue());

                }

                @Test
                @DisplayName("Should return TaskDTO with updated done when done is updated")
                void whenPartialUpdateHaveDoneFieldFill_thenReturnTheTaskUpdatedWithDoneFieldUpdatedAsATaskDTO() {
                        // Arrenge

                        Task taskToUpdate = new Task(taskDTOUpdateExpected, taskDTOUpdateExpected.id());
                        taskToUpdate.setDone(false);

                        TaskDTO partialUpdate = new TaskDTO(null, null, null, null, true);

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(taskToUpdate));
                        when(repository.save(taskArgumentCaptor.capture())).thenReturn(taskUpdatedExpected);

                        // Act

                        TaskDTO taskDTOReturned = taskServices.patchPartialUpdateTask(partialUpdate,
                                        taskToUpdate.getId());

                        // Assert

                        assertNotNull(taskDTOReturned, "The TaskDTO returned is null");
                        assertEquals(taskUpdatedExpected, taskArgumentCaptor.getValue(),
                                        "The Task to delivering to the repository is not equal to the expected Task");
                        assertEquals(new TaskDTO(taskUpdatedExpected), taskDTOReturned,
                                        "The TaskDTO returned is not equal to the expected TaskDTO");

                        // Verify

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(1)).save(taskArgumentCaptor.getValue());

                }

                @Test
                @DisplayName("Should throw NotFoundException when the task to be partially updated is not found")
                void whenTaskNotFound_thenThrowANotFoundException() {
                        // Arrange

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.empty());

                        // Act

                        NotFoundException exceptionReturned = assertThrows(NotFoundException.class,
                                        () -> taskServices.patchPartialUpdateTask(taskDTO, 1L),
                                        "The NotFoundException exception is not thrown when the Task is not found");

                        // Assert

                        assertEquals("Task not found", exceptionReturned.getMessage(),
                                        "The message of the NotFoundException is not equals message expected");
                        assertEquals("It was not possible to find a task with the specified id, try another one.",
                                        exceptionReturned.getDetails(),
                                        "The description of the NotFoundException is not equals message expected");

                        // Verify

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(0)).save(any());

                }

                @Test
                @DisplayName("Should throw IllegalArgumentException when no fields are provided to update")
                void whenPartialUpdateNoHaveFieldsFill_thenThrowAIllegalArgumentException() {
                        // Arrange

                        TaskDTO taskDTOEmpty = new TaskDTO(null, null, null, null, null);

                        // Act

                        IllegalArgumentException exceptionReturned = assertThrows(IllegalArgumentException.class,
                                        () -> taskServices.patchPartialUpdateTask(taskDTOEmpty, 1L),
                                        "The IllegalArgumentException exception is not thrown when the all values of the TaskDTO are null");

                        // Assrt

                        assertEquals("At least one field must be provided to update the task",
                                        exceptionReturned.getMessage(),
                                        "The message of the IllegalArgumentException is not equals message expected");

                        // Verify

                        verify(repository, times(0)).findById(any());
                        verify(repository, times(0)).save(any());

                }

        }

        @Nested
        public class deleteTask {

                @Test
                @DisplayName("Should delete the task when it exists")
                void whenTaskFound_thenReturnNothing() {
                        // Arrange

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.of(task));

                        // Act

                        taskServices.deleteTask(task.getId());

                        // Assert

                        assertEquals(task.getId(), idCArgumentCaptor.getValue(),
                                        "The id passed in the parameter is not the same id of the expected Task");

                        // Verify

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(1)).deleteById(idCArgumentCaptor.getValue());

                }

                @Test
                @DisplayName("Should throw NotFoundException when the task to be deleted is not found")
                void whenTaskNotFound_thenThrowANotFoundException() {
                        // Arrange

                        when(repository.findById(idCArgumentCaptor.capture())).thenReturn(Optional.empty());

                        // Act

                        NotFoundException exceptionReturned = assertThrows(NotFoundException.class,
                                        () -> taskServices.deleteTask(1L),
                                        "The NotFoundException exception is not thrown when the Task is not found");

                        // Assert

                        assertEquals("Task not found", exceptionReturned.getMessage(),
                                        "The message of the NotFoundException is not equals message expected");
                        assertEquals("It was not possible to find a task with the specified id, try another one.",
                                        exceptionReturned.getDetails(),
                                        "The description of the NotFoundException is not equals message expected");

                        // Verify

                        verify(repository, times(1)).findById(idCArgumentCaptor.getValue());
                        verify(repository, times(0)).deleteById(idCArgumentCaptor.getValue());

                }

        }

}
