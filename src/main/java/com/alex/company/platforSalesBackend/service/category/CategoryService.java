package com.alex.company.platforSalesBackend.service.category;

import com.alex.company.platforSalesBackend.dto.category.CategoryRequest;
import com.alex.company.platforSalesBackend.dto.category.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse getCategoryById(Short id);
    List<CategoryResponse> getAllCategories();
    void deleteCategory(Short id);
}
