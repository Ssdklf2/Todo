package com.example.todobackend.controllers;

import com.example.todobackend.DTO.TaskDto;
import com.example.todobackend.model.Task;
import com.example.todobackend.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskByID(@PathVariable String id) {
        return ResponseEntity.ok().body(taskService.getTaskById(id));
    }


    @GetMapping
    public ResponseEntity<List<Task>> getTasks() {
        return ResponseEntity.ok().body(taskService.getList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable String id, @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok().body(taskService.update(id, taskDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable String id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskDto taskDto, UriComponentsBuilder builder) {
        Task task = taskService.create(taskDto);
        return ResponseEntity.created(builder
                .path("tasks")
                .path(String.valueOf(task.getId()))
                .build().toUri()).body(task);
    }
}
