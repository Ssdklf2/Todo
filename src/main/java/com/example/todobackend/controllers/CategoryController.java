package com.example.todobackend.controllers;

import com.example.todobackend.DTO.CategoryDto;
import com.example.todobackend.model.Category;
import com.example.todobackend.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryByID(@PathVariable String id) {
        return ResponseEntity.ok().body(categoryService.getCategoryById(id));
    }


    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok().body(categoryService.getList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String id, @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok().body(categoryService.update(id, categoryDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto,
                                                   UriComponentsBuilder builder) {
        Category category = categoryService.create(categoryDto);
        return ResponseEntity.created(builder
                .path("categories")
                .path(String.valueOf(category.getId()))
                .build().toUri()).body(category);
    }
}
