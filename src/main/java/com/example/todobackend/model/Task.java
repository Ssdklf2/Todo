package com.example.todobackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tasks")
public class Task {
    @Transient
    public final static String SEQUENCE_NAME = "tasks_sequence";

    @Id
    private ObjectId id;
    private String title;
    private boolean completed;
    @DBRef
    private Category category;
    private String priority;
    private LocalDateTime deadline;
}
