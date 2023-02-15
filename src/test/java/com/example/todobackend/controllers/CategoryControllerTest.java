package com.example.todobackend.controllers;

import com.example.todobackend.DTO.CategoryDto;
import com.example.todobackend.exceptions.NotFoundException;
import com.example.todobackend.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private final static String OBJECT_ID = "63e947ecc8e965e7a82bda66";

    @Test
    public void saveCategory_expectStatus_isCreated() throws Exception {
        CategoryDto request = new CategoryDto("someTitle");
        CategoryDto response = new CategoryDto("someTitle");
        response.add(linkTo(methodOn(CategoryController.class)
                .getCategoryByID(OBJECT_ID)).withSelfRel());
        Mockito.when(categoryService.create(request)).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(PATH_CATEGORIES)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("someTitle"))
                .andExpect(jsonPath("$._links.self.href").value(PATH_CATEGORIES + "/" + OBJECT_ID));
    }

    @Test
    void updateCategory_expectStatus_ok() throws Exception {
        CategoryDto request = new CategoryDto("title1");
        CategoryDto response = new CategoryDto("updateTitle");
        response.add(linkTo(methodOn(CategoryController.class)
                .getCategoryByID(OBJECT_ID)).withSelfRel());
        Mockito.when(categoryService.update(OBJECT_ID, request)).thenReturn(response);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(PATH_CATEGORIES + "/" + OBJECT_ID)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("updateTitle"))
                .andExpect(jsonPath("$._links.self.href").value(PATH_CATEGORIES + "/" + OBJECT_ID));
    }

    @Test
    void updateCategory_expectStatus_404() throws Exception {
        CategoryDto response = new CategoryDto("title1");
        Mockito.when(categoryService.update(OBJECT_ID, response))
                .thenThrow(new NotFoundException(NOT_FOUND_MESSAGE));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(PATH_CATEGORIES + "/" + OBJECT_ID)
                                .content(objectMapper.writeValueAsString(response))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
    }

    @Test
    void deleteCategory_expectStatus_ok() throws Exception {
        Mockito.doNothing().when(categoryService).delete(OBJECT_ID);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(PATH_CATEGORIES + "/" + OBJECT_ID)
        ).andExpect(status().isNoContent());
    }

    @Test
    void deleteCategory_expectStatus_404() throws Exception {
        Mockito.doThrow(new NotFoundException(NOT_FOUND_MESSAGE))
                .when(categoryService).delete(OBJECT_ID);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(PATH_CATEGORIES + "/" + OBJECT_ID)
                ).andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
    }


    @Test
    void getById_expectStatus_ok() throws Exception {
        CategoryDto response = new CategoryDto("title1");
        response.add(linkTo(methodOn(CategoryController.class)
                .getCategoryByID(OBJECT_ID)).withSelfRel());
        Mockito.when(categoryService.getCategoryById(OBJECT_ID)).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(PATH_CATEGORIES + "/" + OBJECT_ID)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title1"))
                .andExpect(jsonPath("$._links.self.href").value(PATH_CATEGORIES + "/" + OBJECT_ID));
    }

    @Test
    void getById_expectStatus_404() throws Exception {
        Mockito.when(categoryService.getCategoryById(OBJECT_ID))
                .thenThrow(new NotFoundException(NOT_FOUND_MESSAGE));
        mockMvc.perform(
                        MockMvcRequestBuilders.get(PATH_CATEGORIES + "/" + OBJECT_ID)
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