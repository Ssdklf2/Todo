package com.example.todobackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse extends RepresentationModel<TaskResponse> {
    private String title;
    private boolean completed;
    @JsonProperty("category")
    private CategoryDto category;
    private String priority;
    private String deadline;
}
