package com.example.todobackend.services;

import com.example.todobackend.DTO.TaskDto;
import com.example.todobackend.exceptions.InvalidRequestParameters;
import com.example.todobackend.exceptions.NotFoundException;
import com.example.todobackend.mappers.TaskMapper;
import com.example.todobackend.model.Category;
import com.example.todobackend.model.Task;
import com.example.todobackend.repositories.CategoryRepository;
import com.example.todobackend.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository,
                       CategoryRepository categoryRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.taskMapper = taskMapper;
    }

    public TaskDto update(String id, TaskDto taskDto) {
        Task update = taskMapper.getTaskFromDto(taskDto, getCategoryFromDto(taskDto), checkIdAndGetTask(id).getId());
        Task save = taskRepository.save(update);
        return taskMapper.getDtoFromTask(save);
    }

    public List<TaskDto> getList() {
        return taskRepository.findAll()
                .stream().map(taskMapper::getDtoFromTask).toList();
    }

    public void delete(String id) {
        checkIdAndGetTask(id);
        taskRepository.deleteById(id);
    }

    public TaskDto getTaskById(String id) {
        return taskMapper.getDtoFromTask(checkIdAndGetTask(id));
    }

    public Task create(TaskDto taskDto) {
        Task task = taskMapper.getTaskFromDtoAndGenerateId(taskDto, getCategoryFromDto(taskDto));
        taskRepository.save(task);
        return task;
    }

    private Task checkIdAndGetTask(String id) {
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException("No task with this id"));
    }

    private Category getCategoryFromDto(TaskDto taskDto) {
        Category category = null;
        if (taskDto.getCategoryId() != null) {
            category = categoryRepository.findById(taskDto.getCategoryId())
                    .orElseThrow(() -> new InvalidRequestParameters("Invalid category ID"));
            ;
        }
        return category;
    }
}
