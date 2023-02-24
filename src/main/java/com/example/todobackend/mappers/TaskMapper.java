package com.example.todobackend.mappers;

import com.example.todobackend.DTO.CategoryDto;
import com.example.todobackend.DTO.TaskDto;
import com.example.todobackend.DTO.TaskResponse;
import com.example.todobackend.controllers.CategoryController;
import com.example.todobackend.converters.DateConverter;
import com.example.todobackend.model.Category;
import com.example.todobackend.model.Task;
import com.example.todobackend.model.enums.Priority;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TaskMapper {

    private final DateConverter dateConverter;

    @Autowired
    public TaskMapper(DateConverter dateConverter) {
        this.dateConverter = dateConverter;
    }

    public TaskResponse getTaskResponseFromTask(Task task) {
        CategoryDto categoryDto = null;
        if (task.getCategory() != null) {
            Category category = task.getCategory();
            categoryDto = new CategoryDto(category.getTitle());
            categoryDto.add(linkTo(methodOn(CategoryController.class)
                    .getCategoryByID(category.getId().toString())).withSelfRel());
        }
        return new TaskResponse(
                task.getTitle(),
                task.isCompleted(),
                categoryDto,
                task.getPriority() != null ? task.getPriority().name() : "",
                task.getDeadline()
        );
    }

    public Task getTaskFromDtoAndGenerateId(TaskDto taskDto, Category category) {
        return new Task(
                ObjectId.get(),
                taskDto.getTitle(),
                taskDto.isCompleted(),
                category,
                getEnumPriorityFromString(taskDto.getPriority()),
                taskDto.getDeadline());
    }

    public Task getTaskFromDto(TaskDto taskDto, Category categoryFromDto, ObjectId id) {
        return new Task(
                id,
                taskDto.getTitle(),
                taskDto.isCompleted(),
                categoryFromDto,
                getEnumPriorityFromString(taskDto.getPriority()),
                taskDto.getDeadline());
    }

    public Task updateFields(Task updateTask, TaskDto request, Category category) {
        updateTask.setTitle(request.getTitle());
        updateTask.setCompleted(request.isCompleted());
        updateTask.setCategory(category);
        updateTask.setPriority(getEnumPriorityFromString(request.getPriority()));
        updateTask.setDeadline(request.getDeadline());
        return updateTask;
    }

    public Priority getEnumPriorityFromString(String s) {
        switch (s) {
            case "Низкий" -> {
                return Priority.LOW;
            }
            case "Средний" -> {
                return Priority.MEDIUM;
            }
            case "Высокий" -> {
                return Priority.HIGH;
            }
            case "Очень высокий" -> {
                return Priority.VERY_HIGH;
            }
        }
        return Priority.LOW;
    }
}
