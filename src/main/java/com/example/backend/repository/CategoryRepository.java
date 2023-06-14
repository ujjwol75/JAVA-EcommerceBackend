package com.example.backend.repository;

import com.example.backend.model.Category;
import com.example.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
