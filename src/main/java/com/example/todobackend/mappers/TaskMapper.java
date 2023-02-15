package com.example.todobackend.mappers;

import com.example.todobackend.DTO.TaskDto;
import com.example.todobackend.converters.DateConverter;
import com.example.todobackend.exceptions.InvalidRequestParameters;
import com.example.todobackend.model.Category;
import com.example.todobackend.model.Task;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

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
                categoryFromDto, //getCategoryFromDto(taskDto)
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
