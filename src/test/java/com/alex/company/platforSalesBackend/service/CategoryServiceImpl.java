package com.alex.company.platforSalesBackend.service;
import com.alex.company.platforSalesBackend.dto.category.CategoryRequest;
import com.alex.company.platforSalesBackend.dto.category.CategoryResponse;
import com.alex.company.platforSalesBackend.entity.CategoryEntity;
import com.alex.company.platforSalesBackend.repository.CategoryRepository;
import com.alex.company.platforSalesBackend.service.category.CategoryServiceImpl;
import io.qameta.allure.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Epic("Управление категориями")
@Feature("Сервис категорий")
@DisplayName("Тесты CategoryServiceImpl")
@Owner("Prilepskiy AE")

class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "Electronics";
    private static final String TEST_DESC = "Electronic devices";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Story("Создание новой категории")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("createCategory: корректный запрос -> категория создаётся и возвращается ответ")
    void createCategory_validRequest_success() {
        CategoryRequest request = new CategoryRequest(TEST_NAME, TEST_DESC);
        CategoryEntity savedEntity = prepareSavedCategoryEntity(TEST_ID, TEST_NAME, TEST_DESC);

        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(savedEntity);

        CategoryResponse response = categoryService.createCategory(request);

        verifyRepositorySaveCalled();
        assertThat(response.getCategoryName()).isEqualTo(TEST_NAME);
        assertThat(response.getDescription()).isEqualTo(TEST_DESC);
    }

    /**
     * Вспомогательный метод для подготовки сущности категории.
     * Появляется как шаг в Allure-отчёте.
     */
    @Step("Подготовить сохранённую сущность CategoryEntity: id={id}, name={name}, description={desc}")
    private CategoryEntity prepareSavedCategoryEntity(Long id, String name, String desc) {
        CategoryEntity entity = new CategoryEntity();
        entity.setCategoryId(id);
        entity.setCategoryName(name);
        entity.setDescription(desc);
        return entity;
    }

    @Step("Проверить, что был вызван categoryRepository.save()")
    private void verifyRepositorySaveCalled() {
        verify(categoryRepository).save(any(CategoryEntity.class));
    }

    @Story("Валидация входных данных при создании категории")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("createCategory: null-запрос -> выбрасывается IllegalArgumentException")
    void createCategory_nullRequest_throws() {
        assertThatThrownBy(() -> categoryService.createCategory(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CategoryRequest cannot be null");
    }

    @Test
    @DisplayName("createCategory: пустое название категории -> выбрасывается IllegalArgumentException")
    void createCategory_blankName_throws() {
        CategoryRequest request = new CategoryRequest("", TEST_DESC);
        assertThatThrownBy(() -> categoryService.createCategory(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("categoryName is required");
    }

    @Test
    @DisplayName("createCategory: название длиннее 15 символов -> выбрасывается IllegalArgumentException")
    void createCategory_nameTooLong_throws() {
        String longName = "A".repeat(16);
        CategoryRequest request = new CategoryRequest(longName, TEST_DESC);
        assertThatThrownBy(() -> categoryService.createCategory(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot exceed 15 characters");
    }

    @Story("Получение категории по ID")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("getCategoryById: существующий ID -> возвращается ответ с категорией")
    void getCategoryById_existingId_success() {
        CategoryEntity entity = prepareSavedCategoryEntity(TEST_ID, TEST_NAME, TEST_DESC);

        when(categoryRepository.findById(TEST_ID)).thenReturn(Optional.of(entity));

        CategoryResponse response = categoryService.getCategoryById(TEST_ID);

        verifyRepositoryFindByIdCalled(TEST_ID);
        assertThat(response.getCategoryId()).isEqualTo(TEST_ID);
        assertThat(response.getCategoryName()).isEqualTo(TEST_NAME);
    }

    @Step("Проверить, что был вызван categoryRepository.findById({id})")
    private void verifyRepositoryFindByIdCalled(Long id) {
        verify(categoryRepository).findById(id);
    }

    @Test
    @DisplayName("getCategoryById: несуществующий ID -> выбрасывается EntityNotFoundException")
    void getCategoryById_nonExistingId_throws() {
        when(categoryRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryById(TEST_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Category not found with id");
    }

    @Test
    @DisplayName("getCategoryById: null ID -> выбрасывается IllegalArgumentException")
    void getCategoryById_nullId_throws() {
        assertThatThrownBy(() -> categoryService.getCategoryById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id cannot be null");
    }

    @Story("Получить список всех категорий")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("getAllCategories: возвращает список категорий")
    void getAllCategories_returnsList() {
        CategoryEntity e1 = prepareSavedCategoryEntity(1L, "Books", "Books category");
        CategoryEntity e2 = prepareSavedCategoryEntity(2L, "Clothes", "Clothes category");

        List<CategoryEntity> entities = List.of(e1, e2);
        when(categoryRepository.findAll()).thenReturn(entities);

        List<CategoryResponse> responses = categoryService.getAllCategories();

        verifyRepositoryFindAllCalled();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getCategoryName()).isEqualTo("Books");
        assertThat(responses.get(1).getCategoryName()).isEqualTo("Clothes");
    }

    @Step("Проверить, что был вызван categoryRepository.findAll()")
    private void verifyRepositoryFindAllCalled() {
        verify(categoryRepository).findAll();
    }

    @Story("Удаление категории по ID")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("deleteCategory: существующий ID -> категория удаляется")
    void deleteCategory_existingId_success() {
        categoryService.deleteCategory(TEST_ID);
        verifyRepositoryDeleteByIdCalled(TEST_ID);
    }

    @Step("Проверить, что был вызван categoryRepository.deleteById({id})")
    private void verifyRepositoryDeleteByIdCalled(Long id) {
        verify(categoryRepository).deleteById(id);
    }

    @Test
    @DisplayName("deleteCategory: null ID -> выбрасывается IllegalArgumentException")
    void deleteCategory_nullId_throws() {
        assertThatThrownBy(() -> categoryService.deleteCategory(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id cannot be null");
    }
}