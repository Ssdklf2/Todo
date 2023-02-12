package com.example.todobackend.model;

import lombok.Data;

import java.util.Date;

@Data
public class Task {
    private Integer id;
    private String title;
    private boolean completed;
    private Category category;
    private  Priority priority;
    private Date date;
}
