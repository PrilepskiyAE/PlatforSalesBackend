package com.alex.company.platforSalesBackend.controller.category;

import com.alex.company.platforSalesBackend.dto.category.CategoryRequest;
import com.alex.company.platforSalesBackend.dto.category.CategoryResponse;
import com.alex.company.platforSalesBackend.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/categories")
@Validated
@Tag(name = "Category", description = "API для управления категориями товаров")
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;

    public CategoryControllerImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(
            summary = "Создать категорию",
            description = "Создаёт новую категорию товара"
    )
    @ApiResponse(responseCode = "201", description = "Категория успешно создана")
    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @Override
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request
    ) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить категорию по ID",
            description = "Возвращает категорию товара по её идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Категория найдена")
    @ApiResponse(responseCode = "400", description = "Некорректный ID категории")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    @Override
    public ResponseEntity<CategoryResponse> getCategoryById(
            @Parameter(description = "ID категории", example = "1")
            @PathVariable Short id
    ) {
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Получить все категории",
            description = "Возвращает список всех категорий товаров"
    )
    @ApiResponse(responseCode = "200", description = "Список категорий успешно получен")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @Override
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить категорию",
            description = "Удаляет категорию товара по её идентификатору"
    )
    @ApiResponse(responseCode = "204", description = "Категория успешно удалена")
    @ApiResponse(responseCode = "400", description = "Некорректный ID категории")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    @Override
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID категории", example = "1")
            @PathVariable Short id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}