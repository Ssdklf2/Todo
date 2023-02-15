package com.example.todobackend.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto extends RepresentationModel<TaskDto> {
    private String title;
    private boolean completed;
    private String categoryId;
    private String priority;
    private String deadline;
}
