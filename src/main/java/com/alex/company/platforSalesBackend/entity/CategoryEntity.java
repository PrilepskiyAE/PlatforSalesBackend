package com.alex.company.platforSalesBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Category")
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", nullable = false, length = 15)
    private String categoryName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

   // @Lob
    @Column(name = "picture", columnDefinition = "BYTEA")
    private byte[] picture;
}
