package com.alex.company.platforSalesBackend.service.category;

import com.alex.company.platforSalesBackend.dto.category.CategoryRequest;
import com.alex.company.platforSalesBackend.dto.category.CategoryResponse;
import com.alex.company.platforSalesBackend.entity.CategoryEntity;
import com.alex.company.platforSalesBackend.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("CategoryRequest cannot be null");
        }
        if (request.getCategoryName() == null || request.getCategoryName().isBlank()) {
            throw new IllegalArgumentException("categoryName is required and cannot be blank");
        }
        if (request.getCategoryName().length() > 15) {
            throw new IllegalArgumentException("categoryName cannot exceed 15 characters");
        }

        CategoryEntity entity = new CategoryEntity(
                    null,
                    request.getCategoryName(),
                    request.getDescription(),
                    null
            );

        CategoryEntity saved = categoryRepository.save(entity);

        return new CategoryResponse(
                saved.getCategoryId(),
                saved.getCategoryName(),
                saved.getDescription()
        );
    }

    @Override
    public CategoryResponse getCategoryById(Short id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

        return categoryRepository.findById(id).map( it -> new CategoryResponse(
                it.getCategoryId(),
                it.getCategoryName(),
                it.getDescription())

        ).orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));



    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map( it -> new CategoryResponse(
                it.getCategoryId(),
                it.getCategoryName(),
                it.getDescription())

        ).toList();
    }

    @Override
    public void deleteCategory(Short id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        categoryRepository.deleteById(id);
    }
}
