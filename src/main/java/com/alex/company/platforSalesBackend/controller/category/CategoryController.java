package com.alex.company.platforSalesBackend.controller.category;

import com.alex.company.platforSalesBackend.dto.category.CategoryRequest;
import com.alex.company.platforSalesBackend.dto.category.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CategoryController {

    ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request);

    ResponseEntity<CategoryResponse> getCategoryById(Long id);

    ResponseEntity<List<CategoryResponse>> getAllCategories();

    ResponseEntity<Void> deleteCategory(Long id);
}
