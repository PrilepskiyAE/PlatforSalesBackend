package com.alex.company.platforSalesBackend.repository;

import com.alex.company.platforSalesBackend.entity.CategoryEntity;
import com.alex.company.platforSalesBackend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,  Short> {
    Optional<CategoryEntity> findByCategoryName(String categoryName);

    List<CategoryEntity> findByCategoryNameContainingIgnoreCase(String fragment);

    Page<CategoryEntity> findAllByOrderByCategoryNameAsc(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.description IS NOT NULL")
    List<CategoryEntity> findCategoriesWithDescription();
}
