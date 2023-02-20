package com.example.todobackend.mappers;

import com.example.todobackend.DTO.CategoryDto;
import com.example.todobackend.DTO.TaskDto;
import com.example.todobackend.DTO.TaskResponse;
import com.example.todobackend.controllers.CategoryController;
import com.example.todobackend.converters.DateConverter;
import com.example.todobackend.exceptions.InvalidRequestParameters;
import com.example.todobackend.model.Category;
import com.example.todobackend.model.Task;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TaskMapper {

    private final DateConverter dateConverter;

    @Autowired
    public TaskMapper(DateConverter dateConverter) {
        this.dateConverter = dateConverter;
    }

    public TaskDto getDtoFromTask(Task task) {
        String category = null;
        if (task.getCategory() != null) {
            category = task.getCategory().getId().toString();
        }
        String time = null;
        if (task.getDeadline() != null) {
            time = dateConverter.getStringFromTime(task.getDeadline());
        }
        return new TaskDto(
                task.getTitle(),
                task.isCompleted(),
                category,
                task.getPriority(),
                time
        );
    }

    public TaskResponse getTaskResponseFromTask(Task task) {
        CategoryDto categoryDto = null;
        if (task.getCategory() != null) {
            Category category = task.getCategory();
            categoryDto = new CategoryDto(category.getTitle());
            categoryDto.add(linkTo(methodOn(CategoryController.class)
                    .getCategoryByID(category.getId().toString())).withSelfRel());
        }
        String time = null;
        if (task.getDeadline() != null) {
            time = dateConverter.getStringFromTime(task.getDeadline());
        }
        return new TaskResponse(
                task.getTitle(),
                task.isCompleted(),
                categoryDto,
                task.getPriority(),
                time
        );
    }

    public Task getTaskFromDtoAndGenerateId(TaskDto taskDto, Category categoryFromDto) {
        return new Task(
                ObjectId.get(),
                taskDto.getTitle(),
                taskDto.isCompleted(),
                categoryFromDto,
                taskDto.getPriority(),
                getLocalDateTimeFromDto(taskDto));
    }

    public Task getTaskFromDto(TaskDto taskDto, Category categoryFromDto, ObjectId id) {
        return new Task(
                id,
                taskDto.getTitle(),
                taskDto.isCompleted(),
                categoryFromDto,
                taskDto.getPriority(),
                getLocalDateTimeFromDto(taskDto));
    }

    public Task updateFields(Task updateTask, TaskDto request, Category category) {
        updateTask.setTitle(request.getTitle());
        updateTask.setCompleted(request.isCompleted());
        updateTask.setCategory(category);
        updateTask.setPriority(request.getPriority());
        updateTask.setDeadline(getLocalDateTimeFromDto(request));
        return updateTask;
    }

    private LocalDateTime getLocalDateTimeFromDto(TaskDto taskDto) {
        LocalDateTime deadline;
        try {
            deadline = dateConverter.getTimeFromString(taskDto.getDeadline());
        } catch (DateTimeParseException ex) {
            throw new InvalidRequestParameters("Invalid date format. Correct: yyyy-MM-dd HH:mm:ss");
        }
        return deadline;
    }
}
