package com.example.todobackend.services;

import com.example.todobackend.DTO.TaskDto;
import com.example.todobackend.DTO.TaskResponse;
import com.example.todobackend.controllers.TaskController;
import com.example.todobackend.exceptions.NotFoundException;
import com.example.todobackend.mappers.TaskMapper;
import com.example.todobackend.model.Category;
import com.example.todobackend.model.Task;
import com.example.todobackend.repositories.CategoryRepository;
import com.example.todobackend.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    public TaskResponse update(String id, TaskDto request) {
        Task updateTask = checkIdAndGetTaskFromRepository(id);
        Category category = getCategoryFromDto(request);
        updateTask = taskMapper.updateFields(updateTask, request, category);
        Task taskSave = taskRepository.save(updateTask);
        return getTaskDtoWithSelfLink(taskSave);
    }

    public List<TaskResponse> getList() {
        return taskRepository.findAll().stream()
                .map(this::getTaskDtoWithSelfLink)
                .toList();
    }

    public void delete(String id) {
        checkIdAndGetTaskFromRepository(id);
        taskRepository.deleteById(id);
    }

    public TaskResponse getTaskById(String id) {
        Task task = checkIdAndGetTaskFromRepository(id);
        return getTaskDtoWithSelfLink(task);
    }

    public TaskResponse create(TaskDto taskDto) {
        Task task = taskMapper.getTaskFromDtoAndGenerateId(taskDto, getCategoryFromDto(taskDto));
        return getTaskDtoWithSelfLink(taskRepository.save(task));
    }

    private Task checkIdAndGetTaskFromRepository(String id) {
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException("No task with this id"));
    }

    /**
     * Возвращает категорию из запроса(DTO)
     *
     * @param taskDto - DTO
     * @return если в Dto есть категория: получить существующую категорию из ДБ, иначе выбросить ошибку:
     * InvalidRequestParameters (неправильный ID категории);
     * null - если категория отсутствует
     */
    private Category getCategoryFromDto(TaskDto taskDto) {
        Category category = null;
        if (taskDto.getCategory() != null) {
            category = categoryRepository.findByTitle(taskDto.getCategory());
        }
        return category;
    }

    private TaskResponse getTaskDtoWithSelfLink(Task task) {
        TaskResponse response = taskMapper.getTaskResponseFromTask(task);
        setSelfLink(response, task.getId().toString());
        return response;
    }

    private void setSelfLink(TaskResponse response, String id) {
        Link selfLink = linkTo(methodOn(TaskController.class)
                .getTaskByID(id)).withSelfRel();
        response.add(selfLink);
    }
}
