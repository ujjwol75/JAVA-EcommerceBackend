package com.example.backend.service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.model.Category;
import com.example.backend.payload.CategoryDto;
import com.example.backend.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    public CategoryDto createCategory(CategoryDto categoryDto) {
        //CategoryDto to Category
        Category cat = this.mapper.map(categoryDto, Category.class);
        Category save = this.categoryRepository.save(cat);

        //Category to CategoryDto
        return this.mapper.map(save, CategoryDto.class);
    }


    public CategoryDto updateCategory(int id, CategoryDto newCat) {
        Category oldCat = this.categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("This category id is not found..."));
        oldCat.setTitle(newCat.getTitle());
        Category save = this.categoryRepository.save(oldCat);
        return this.mapper.map(save, CategoryDto.class);
    }

    public void deleteCategory(int id) {
        Category cat = this.categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("This category id is not found..."));
        this.categoryRepository.delete(cat);
    }

    public CategoryDto getById(int id) {
        Category cat = this.categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("This category id is not found..."));
        return this.mapper.map(cat, CategoryDto.class);

    }

    public List<CategoryDto> getAllCategory() {

        List<Category> all = this.categoryRepository.findAll();
        List<CategoryDto> allDto = all.stream().map(cat -> this.mapper.map(cat, CategoryDto.class)).collect(Collectors.toList());
        return allDto;
    }


}
