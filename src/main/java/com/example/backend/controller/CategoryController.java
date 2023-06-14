package com.example.backend.controller;

import com.example.backend.payload.ApiResponse;
import com.example.backend.payload.CategoryDto;
import com.example.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController{

    @Autowired
    private CategoryService categoryService;

    //Create Category
    @GetMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
        CategoryDto category = this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<CategoryDto>(category, HttpStatus.CREATED);
    }


    //Update Category
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable int id, @RequestBody CategoryDto categoryDto){
        CategoryDto categoryDto1 = this.categoryService.updateCategory(id, categoryDto);
        return new ResponseEntity<CategoryDto>(categoryDto1, HttpStatus.OK);
    }

   //delete Category
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable int id){
        this.categoryService.deleteCategory(id);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Categories deleted Successfully...",true), HttpStatus.OK );
    }

    // Get category by id
    @GetMapping("/getById/{id}")
    public ResponseEntity<CategoryDto> getById(@PathVariable int id){
        CategoryDto byId = this.categoryService.getById(id);
        return new ResponseEntity<CategoryDto>(byId, HttpStatus.OK);

    }


    //Get all category
    @GetMapping("/getAll")
    public ResponseEntity<List<CategoryDto>> getAllCategory(){
        List<CategoryDto> allCategory = this.categoryService.getAllCategory();
        return new ResponseEntity<List<CategoryDto>>(allCategory, HttpStatus.OK);
    }

}
