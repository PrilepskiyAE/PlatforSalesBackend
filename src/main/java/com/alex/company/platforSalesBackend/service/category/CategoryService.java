package com.alex.company.platforSalesBackend.service.category;

import com.alex.company.platforSalesBackend.dto.CategoryRequest;
import com.alex.company.platforSalesBackend.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse getCategoryById(Short id);
    List<CategoryResponse> getAllCategories();
    void deleteCategory(Short id);
}
