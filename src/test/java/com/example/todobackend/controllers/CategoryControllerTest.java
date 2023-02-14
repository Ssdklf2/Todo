package com.example.todobackend.controllers;

import com.example.todobackend.DTO.CategoryDto;
import com.example.todobackend.exceptions.NotFoundException;
import com.example.todobackend.model.Category;
import com.example.todobackend.services.CategoryService;
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

@WebMvcTest({CategoryController.class})
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;
    @Autowired
    private ObjectMapper objectMapper;

    private final static String PATH_CATEGORIES = "/categories";
    private final static String NOT_FOUND_MESSAGE = "Category with this id does not exist";
    private final static String id = "63e947ecc8e965e7a82bda66";

    @Test
    public void saveCategory_expectStatus_isCreated() throws Exception {
        Category category = new Category(ObjectId.get(), "someTitle");
        Mockito.when(categoryService.create(Mockito.any())).thenReturn(category);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(PATH_CATEGORIES)
                        .content(objectMapper.writeValueAsString(category))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(category)));
    }

    @Test
    void updateCategory_expectStatus_ok() throws Exception {
        CategoryDto categoryDto = new CategoryDto("title1");
        Mockito.when(categoryService.update(id, categoryDto)).thenReturn(categoryDto);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(PATH_CATEGORIES + "/" + id)
                                .content(objectMapper.writeValueAsString(categoryDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(categoryDto)));
    }

    @Test
    void updateCategory_expectStatus_404() throws Exception {
        CategoryDto categoryDto = new CategoryDto("title1");
        Mockito.when(categoryService.update(id, categoryDto))
                .thenThrow(new NotFoundException(NOT_FOUND_MESSAGE));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(PATH_CATEGORIES + "/" + id)
                                .content(objectMapper.writeValueAsString(categoryDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
    }

    @Test
    void deleteCategory_expectStatus_ok() throws Exception {
        Mockito.doNothing().when(categoryService).delete(id);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(PATH_CATEGORIES + "/" + id)
        ).andExpect(status().isNoContent());
    }

    @Test
    void deleteCategory_expectStatus_404() throws Exception {
        Mockito.doThrow(new NotFoundException(NOT_FOUND_MESSAGE))
                .when(categoryService).delete(id);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(PATH_CATEGORIES + "/" + id)
                ).andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
    }


    @Test
    void getById_expectStatus_ok() throws Exception {
        CategoryDto categoryDto = new CategoryDto("title1");
        Mockito.when(categoryService.getCategoryById(id)).thenReturn(categoryDto);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(PATH_CATEGORIES + "/" + id)
                ).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(categoryDto)));
    }

    @Test
    void getById_expectStatus_404() throws Exception {
        Mockito.when(categoryService.getCategoryById(id))
                .thenThrow(new NotFoundException(NOT_FOUND_MESSAGE));
        mockMvc.perform(
                        MockMvcRequestBuilders.get(PATH_CATEGORIES + "/" + id)
                ).andExpect(status().isNotFound())
                .andExpect(mvcResult ->
                        mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
    }

    @Test
    void getCategories_expectStatus_ok() throws Exception {
        CategoryDto categoryDto = new CategoryDto("title");
        CategoryDto categoryDto1 = new CategoryDto("title1");
        CategoryDto categoryDto2 = new CategoryDto("title2");
        List<CategoryDto> list = Arrays.asList(categoryDto, categoryDto1, categoryDto2);
        Mockito.when(categoryService.getList())
                .thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(PATH_CATEGORIES)
                ).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }
}