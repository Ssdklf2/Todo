package com.example.todobackend.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private String title;
    private String category;
    private String priority;
    private LocalDate deadline;
    private boolean completed;
}
