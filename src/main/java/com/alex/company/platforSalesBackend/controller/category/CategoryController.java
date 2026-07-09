package com.alex.company.platforSalesBackend.controller.category;

import com.alex.company.platforSalesBackend.dto.category.CategoryRequest;
import com.alex.company.platforSalesBackend.dto.category.CategoryResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryController {

    ResponseEntity<CategoryResponse> createCategory(CategoryRequest request);

    ResponseEntity<CategoryResponse> getCategoryById(Short id);

    ResponseEntity<List<CategoryResponse>> getAllCategories();

    ResponseEntity<Void> deleteCategory(Short id);
}
