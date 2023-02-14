package com.example.todobackend.services;

import com.example.todobackend.DTO.CategoryDto;
import com.example.todobackend.exceptions.NotFoundException;
import com.example.todobackend.model.Category;
import com.example.todobackend.repositories.CategoryRepository;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper = new ModelMapper();


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(CategoryDto categoryDto) {
        Category category = mapper.map(categoryDto, Category.class);
        category.setId(ObjectId.get());
        return categoryRepository.save(category);
    }

    public void delete(String id) {
        getCategory(id);
        categoryRepository.deleteById(id);
    }

    public CategoryDto update(String id, CategoryDto categoryDto) {
        Category category = getCategory(id);
        category.setTitle(categoryDto.getTitle());
        Category save = categoryRepository.save(category);
        return mapper.map(save, CategoryDto.class);
    }

    public List<CategoryDto> getList() {
        return categoryRepository.findAll().stream()
                .map(category -> mapper.map(category, CategoryDto.class)).toList();
    }

    public CategoryDto getCategoryById(String id) {
        Category category = getCategory(id);
        return mapper.map(category, CategoryDto.class);
    }

    private Category getCategory(String id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Category with this id does not exist"));
    }
}
