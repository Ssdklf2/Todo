package com.example.todobackend.model;

import com.example.todobackend.model.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tasks")
public class Task {

    @Id
    private ObjectId id;
    private String title;
    private boolean completed;
    @DBRef
    private Category category;
    private Priority priority;
    private LocalDate deadline;
}
