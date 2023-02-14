package com.example.todobackend.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private String title;
    private boolean completed;
    private String categoryId;
    private String priority;
    //    (validation HERE) format date: yyyy-MM-dd HH:mm:ss
    private String deadline;
}
