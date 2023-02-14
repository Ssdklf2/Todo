package com.example.todobackend.controllers;

import com.example.todobackend.DTO.TaskDto;
import com.example.todobackend.exceptions.NotFoundException;
import com.example.todobackend.model.Task;
import com.example.todobackend.model.enums.Priority;
import com.example.todobackend.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;
    @Autowired
    private ObjectMapper objectMapper;

    private final static String PATH_TASKS = "/tasks";
    private final static String NOT_FOUND_MESSAGE = "No task with this id";
    private final static String OBJECT_ID = "63e947ecc8e965e7a82bda66";


    @Test
    public void saveTask_expectStatus_isCreated() throws Exception {
        TaskDto taskDto = new TaskDto("someTitle", true, "123", Priority.LOW.name(), null);
        Task task = new Task(ObjectId.get(), "someTitle", true, null, null, null);
        Mockito.when(taskService.create(taskDto)).thenReturn(task);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(PATH_TASKS)
                        .content(objectMapper.writeValueAsString(taskDto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(task)));
    }

    @Test
    void updateTask_expectStatus_ok() throws Exception {
        TaskDto taskDto = new TaskDto("someTitle", true, null, null, null);
        Mockito.when(taskService.update(OBJECT_ID, taskDto)).thenReturn(taskDto);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(PATH_TASKS + "/" + OBJECT_ID)
                                .content(objectMapper.writeValueAsString(taskDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskDto)));
    }


    @Test
    void updateTask_expectStatus_404() throws Exception {
        TaskDto taskDTO = new TaskDto("someTitle", true, null, null, null);
        Mockito.when(taskService.update(OBJECT_ID, taskDTO))
                .thenThrow(new NotFoundException(NOT_FOUND_MESSAGE));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(PATH_TASKS + "/" + OBJECT_ID)
                                .content(objectMapper.writeValueAsString(taskDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
    }

    @Test
    void deleteTask_expectStatus_ok() throws Exception {
        TaskDto taskDTO = new TaskDto("someTitle", true, null, null, null);
        Mockito.doNothing().when(taskService).delete(OBJECT_ID);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(PATH_TASKS + "/" + OBJECT_ID)
        ).andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_expectStatus_404() throws Exception {
        Mockito.doThrow(new NotFoundException(NOT_FOUND_MESSAGE))
                .when(taskService).delete(OBJECT_ID);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(PATH_TASKS + "/" + OBJECT_ID)
                ).andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
    }


    @Test
    void getById_expectStatus_ok() throws Exception {
        TaskDto taskDTO = new TaskDto("someTitle", true, null, null, null);
        Mockito.when(taskService.getTaskById(OBJECT_ID)).thenReturn(taskDTO);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(PATH_TASKS + "/" + OBJECT_ID)
                ).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskDTO)));
    }

    @Test
    void getById_expectStatus_404() throws Exception {
        Mockito.when(taskService.getTaskById(OBJECT_ID))
                .thenThrow(new NotFoundException(NOT_FOUND_MESSAGE));
        mockMvc.perform(
                        MockMvcRequestBuilders.get(PATH_TASKS + "/" + OBJECT_ID)
                ).andExpect(status().isNotFound())
                .andExpect(mvcResult ->
                        mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
    }

    @Test
    void getTasks_expectStatus_ok() throws Exception {
        TaskDto taskDTO = new TaskDto("someTitle", true, null, null, null);
        TaskDto taskDTO1 = new TaskDto("someTitle1", true, null, null, null);
        TaskDto taskDTO2 = new TaskDto("someTitle2", true, null, null, null);
//        Task task = new Task(ObjectId.get(), "someTitle", true, null, null, null);
//        Task task1 = new Task(ObjectId.get(), "someTitle1", true, null, null, null);
//        Task task2 = new Task(ObjectId.get(), "someTitle2", true, null, null, null);
//        List<Task> list = Arrays.asList(task, task1, task2);
        List<TaskDto> list = Arrays.asList(taskDTO, taskDTO1, taskDTO2);
        Mockito.when(taskService.getList())
                .thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(PATH_TASKS)
                ).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }
}