package br.com.todo.todo.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.todo.todo.dto.TaskDTO;
import br.com.todo.todo.exceptions.NotFoundException;
import br.com.todo.todo.services.TaskServices;

@WebMvcTest(TaskController.class)
public class TaskControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskServices taskServices;

    private Long validId;
    private Long invalidId;
    private TaskDTO validTaskDTO;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        validId = 1L;
        invalidId = 999L;
        validTaskDTO = new TaskDTO(validId, "Task name", "Task description", 2, false);
        objectMapper = new ObjectMapper();
    }

    @Nested
    class GetAllTasksTests {

        @Test
        public void whenGetAllTasks_thenReturns200() throws Exception {
            // Arrange

            when(taskServices.getAllTasks()).thenReturn(Collections.emptyList());

            // Act

            mockMvc.perform(get("/todo-list")
                    .contentType(MediaType.APPLICATION_JSON))
                    // Assert
                    .andExpect(status().isOk());

            // Verify

            verify(taskServices, times(1)).getAllTasks();

        }

    }

    @Nested
    class GetTaskByIdTests {

        @Test
        public void whenValidId_thenReturns200() throws Exception {
            // Arrange

            when(taskServices.getTaskById(validId)).thenReturn(validTaskDTO);

            // Act

            mockMvc.perform(get("/todo-list/{id}", validId)
                    .contentType(MediaType.APPLICATION_JSON))
                    // Assert
                    .andExpect(status().isOk());

            // Verify

            verify(taskServices, times(1)).getTaskById(validId);

        }

        @Test
        public void whenInvalidId_thenReturns404() throws Exception {
            // Arrange

            when(taskServices.getTaskById(invalidId)).thenThrow(new NotFoundException("Task not found",
                    "It was not possible to find a task with the specified id, try another one."));

            // Act

            mockMvc.perform(get("/todo-list/{id}", invalidId)
                    .contentType(MediaType.APPLICATION_JSON))
                    // Assert
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("Task not found"))
                    .andExpect(jsonPath("$.description")
                            .value("It was not possible to find a task with the specified id, try another one."));

            // Verify

            verify(taskServices, times(1)).getTaskById(invalidId);

        }

    }

    @Nested
    class GetAllDoneTasksTests {

        @Test
        public void whenGetAllDoneTasks_thenReturns200() throws Exception {
            // Arrange

            when(taskServices.getAllDoneTasks()).thenReturn(Collections.emptyList());

            // Act

            mockMvc.perform(get("/todo-list/done")
                    .contentType(MediaType.APPLICATION_JSON))
                    // Assert
                    .andExpect(status().isOk());

            // verify

            verify(taskServices, times(1)).getAllDoneTasks();

        }

    }

    @Nested
    class GetAllPendingTasksTests {

        @Test
        public void whenGetAllPendingTasks_thenReturns200() throws Exception {
            // Arrange

            when(taskServices.getAllPendingTasks()).thenReturn(Collections.emptyList());

            // Act

            mockMvc.perform(get("/todo-list/pending")
                    .contentType(MediaType.APPLICATION_JSON))
                    // Assert
                    .andExpect(status().isOk());

            // Verify

            verify(taskServices, times(1)).getAllPendingTasks();

        }

    }

    @Nested
    class PostCreateTaskTests {

        @Test
        public void whenValidTask_thenReturns201() throws Exception {
            // Arrange

            TaskDTO validTaskDTOWithoutId = new TaskDTO(null, "Task name", "Task description", 2, false);
            String validRequest = objectMapper.writeValueAsString(validTaskDTOWithoutId);

            when(taskServices.postCreateTask(validTaskDTOWithoutId)).thenReturn(validTaskDTO);

            // Act

            mockMvc.perform(post("/todo-list")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(validRequest))
                    // Assert
                    .andExpect(status().isCreated());

            // Verify

            verify(taskServices, times(1)).postCreateTask(validTaskDTOWithoutId);

        }

        @Test
        public void whenInvalidTask_thenReturns400() throws Exception {
            // Arrange

            TaskDTO invalidTaskDTOWithoutId = new TaskDTO(null, " ", null, null, null);
            String invalidJson = objectMapper.writeValueAsString(invalidTaskDTOWithoutId);

            // Act

            mockMvc.perform(post("/todo-list")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson))
                    // Assert
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("Validation errors"))
                    .andExpect(jsonPath("$.errors.name").value("Name must be filled with characters"))
                    .andExpect(jsonPath("$.errors.description").value("Description must be filled with characters"))
                    .andExpect(jsonPath("$.errors.priority").value("Priority cannot be null"))
                    .andExpect(jsonPath("$.errors.done").value("Done cannot be null"));

            // Verify

            verify(taskServices, never()).postCreateTask(invalidTaskDTOWithoutId);

        }

    }

    @Nested
    class PutUpdateTaskTests {

        @Test
        public void whenValidTask_thenReturns200() throws Exception {
            // Arrange

            TaskDTO validTaskDTOWithoutId = new TaskDTO(null, "Task name", "Task description", 2, false);
            String validRequest = objectMapper.writeValueAsString(validTaskDTOWithoutId);

            when(taskServices.putUpdateTask(validTaskDTOWithoutId, validId)).thenReturn(validTaskDTO);

            // Act

            mockMvc.perform(put("/todo-list/{id}", validId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(validRequest))
                    // Assert
                    .andExpect(status().isOk());

            // Verify

            verify(taskServices, times(1)).putUpdateTask(validTaskDTOWithoutId, validId);

        }

        @Test
        public void whenInvalidTask_thenReturns400() throws Exception {
            // Arrange

            TaskDTO invalidTaskDTOWithoutId = new TaskDTO(null, "", null, -3, null);
            String invalidJson = objectMapper.writeValueAsString(invalidTaskDTOWithoutId);

            // Act

            mockMvc.perform(put("/todo-list/{id}", validId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson))
                    // Assert
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("Validation errors"))
                    .andExpect(jsonPath("$.errors.name").value("Name must be filled with characters"))
                    .andExpect(jsonPath("$.errors.description").value("Description must be filled with characters"))
                    .andExpect(jsonPath("$.errors.priority").value("Priority cannot be negative or zero"))
                    .andExpect(jsonPath("$.errors.done").value("Done cannot be null"));

            // Verify

            verify(taskServices, never()).putUpdateTask(invalidTaskDTOWithoutId, validId);

        }

        @Test
        public void whenTaskNotFound_thenReturns404() throws Exception {
            // Arrange

            TaskDTO validTaskDTOWithoutId = new TaskDTO(null, "Task name", "Task description", 2, false);
            String validRequest = objectMapper.writeValueAsString(validTaskDTOWithoutId);

            when(taskServices.putUpdateTask(validTaskDTOWithoutId, invalidId))
                    .thenThrow(new NotFoundException("Task not found",
                            "It was not possible to find a task with the specified id, try another one."));

            // Act

            mockMvc.perform(put("/todo-list/{id}", invalidId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(validRequest))
                    // Assert
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("Task not found"))
                    .andExpect(jsonPath("$.description")
                            .value("It was not possible to find a task with the specified id, try another one."));

            // Verify

            verify(taskServices, times(1)).putUpdateTask(validTaskDTOWithoutId, invalidId);

        }

    }

    @Nested
    class PatchPartialUpdateTaskTests {

        @Test
        public void whenValidTask_thenReturns200() throws Exception {
            // Arrange

            TaskDTO validTaskDTOWithoutId = new TaskDTO(null, null, "Task  description", null, false);
            String validRequest = objectMapper.writeValueAsString(validTaskDTOWithoutId);

            when(taskServices.patchPartialUpdateTask(validTaskDTOWithoutId, validId)).thenReturn(validTaskDTO);

            // Act

            mockMvc.perform(patch("/todo-list/{id}", validId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(validRequest))
                    // Assert
                    .andExpect(status().isOk());

            // Verify

            verify(taskServices, times(1)).patchPartialUpdateTask(validTaskDTOWithoutId, validId);

        }

        @Test
        public void whenInvalidTask_thenReturns400() throws Exception {
            // Arrange

            TaskDTO invalidTaskDTOWithoutId = new TaskDTO(null, null, null, null, null);
            String invalidJson = objectMapper.writeValueAsString(invalidTaskDTOWithoutId);

            when(taskServices.patchPartialUpdateTask(invalidTaskDTOWithoutId, validId))
                    .thenThrow(new IllegalArgumentException("At least one field must be provided to update the task"));

            // Act

            mockMvc.perform(patch("/todo-list/{id}", validId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson))
                    // Assert
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("At least one field must be provided to update the task"))
                    .andExpect(jsonPath("$.description")
                            .value("To carry out a partial task update, at least one task field must be supplied"));

            // Verify

            verify(taskServices, times(1)).patchPartialUpdateTask(invalidTaskDTOWithoutId, validId);

        }

        @Test
        public void whenTaskNotFound_thenReturns404() throws Exception {
            // Arrange

            TaskDTO validTaskDTOWithoutId = new TaskDTO(null, null, null, null, false);
            String validRequest = objectMapper.writeValueAsString(validTaskDTOWithoutId);

            when(taskServices.patchPartialUpdateTask(validTaskDTOWithoutId, invalidId))
                    .thenThrow(new NotFoundException("Task not found",
                            "It was not possible to find a task with the specified id, try another one."));

            // Act

            mockMvc.perform(patch("/todo-list/{id}", invalidId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(validRequest))
                    // Assert
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("Task not found"))
                    .andExpect(jsonPath("$.description")
                            .value("It was not possible to find a task with the specified id, try another one."));

            // Verify

            verify(taskServices, times(1)).patchPartialUpdateTask(validTaskDTOWithoutId, invalidId);

        }

    }

    @Nested
    class DeleteTaskTests {

        @Test
        public void whenTaskFound_thenReturns204() throws Exception {
            // Arrange
            // (No additional preparation required for this test)

            // Act
            mockMvc.perform(delete("/todo-list/{id}", validId))
                    // Assert
                    .andExpect(status().isNoContent());

            // Verify

            verify(taskServices, times(1)).deleteTask(validId);

        }

        @Test
        public void whenTaskNotFound_thenReturns404() throws Exception {
            // Arrange

            doThrow(new NotFoundException("Task not found",
                    "It was not possible to find a task with the specified id, try another one."))
                    .when(taskServices)
                    .deleteTask(invalidId);

            // Act

            mockMvc.perform(delete("/todo-list/{id}", invalidId))
                    // Assert
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("Task not found"))
                    .andExpect(jsonPath("$.description")
                            .value("It was not possible to find a task with the specified id, try another one."));

            // Verify

            verify(taskServices, times(1)).deleteTask(invalidId);

        }

    }
}
