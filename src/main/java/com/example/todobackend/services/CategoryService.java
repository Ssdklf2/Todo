package com.example.todobackend.services;

import com.example.todobackend.DTO.CategoryDto;
import com.example.todobackend.controllers.CategoryController;
import com.example.todobackend.exceptions.NotFoundException;
import com.example.todobackend.model.Category;
import com.example.todobackend.repositories.CategoryRepository;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper = new ModelMapper();


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto create(CategoryDto categoryDto) {
        Category category = new Category();
        category.setTitle(categoryDto.getTitle());
        category.setId(ObjectId.get());

        Category save = categoryRepository.save(category);

        CategoryDto response = new CategoryDto(save.getTitle());
        String id = String.valueOf(save.getId());
        setSelfLink(response, id);
        return response;
    }

    public void delete(String id) {
        getCategory(id);
        categoryRepository.deleteById(id);
    }

    public CategoryDto update(String id, CategoryDto categoryDto) {
        Category category = getCategory(id);
        category.setTitle(categoryDto.getTitle());
        Category save = categoryRepository.save(category);
        CategoryDto response = mapper.map(save, CategoryDto.class);
        setSelfLink(response, save.getId().toString());
        return response;
    }

    public List<CategoryDto> getList() {
        return categoryRepository.findAll().stream()
                .map(this::getCategoryDtoWithLinks)
                .toList();
    }

    public CategoryDto getCategoryById(String id) {
        Category category = getCategory(id);
        return mapper.map(category, CategoryDto.class);
    }

    private Category getCategory(String id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Category with this id does not exist"));
    }

    private CategoryDto getCategoryDtoWithLinks(Category category) {
        CategoryDto response = mapper.map(category, CategoryDto.class);
        setSelfLink(response, String.valueOf(category.getId()));
        return response;
    }

    private static void setSelfLink(CategoryDto response, String id) {
        Link selfLink = linkTo(methodOn(CategoryController.class)
                .getCategoryByID(id)).withSelfRel();
        response.add(selfLink);
    }
}
