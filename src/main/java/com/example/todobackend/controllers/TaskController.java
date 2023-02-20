package com.example.todobackend.controllers;

import com.example.todobackend.DTO.TaskDto;
import com.example.todobackend.DTO.TaskResponse;
import com.example.todobackend.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:4200/")
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskByID(@PathVariable String id) {
        return ResponseEntity.ok().body(taskService.getTaskById(id));
    }


    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks() {
        return ResponseEntity.ok().body(taskService.getList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable String id, @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok().body(taskService.update(id, taskDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable String id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskDto taskDto, UriComponentsBuilder builder) {
        TaskResponse response = taskService.create(taskDto);
        String selfLink = response.getLink("self").orElse(Link.of("self")).toString();
        return ResponseEntity.created(builder
                .path(selfLink)
                .build().toUri()).body(response);
    }
}
