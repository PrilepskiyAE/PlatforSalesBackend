package com.alex.company.platforSalesBackend.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Short categoryId;
    private String categoryName;
    private String description;
}
