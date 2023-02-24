package com.example.todobackend.repositories;

import com.example.todobackend.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findByTitle(String title);
}
