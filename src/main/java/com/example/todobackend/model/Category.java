package com.example.todobackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "categories")
public class Category {
    @Transient
    public final static String SEQUENCE_NAME = "categories_sequence";

    @Id
    private ObjectId id;
    private String title;
}
