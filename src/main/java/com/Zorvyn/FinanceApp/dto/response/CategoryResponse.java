package com.Zorvyn.FinanceApp.dto.response;

import java.time.LocalDateTime;

import com.Zorvyn.FinanceApp.enums.CategoryType;

public class CategoryResponse {
    private Long id;
    private String name;
    private CategoryType type;
    private LocalDateTime createdAt;

    public CategoryResponse() {}

    public CategoryResponse(Long id, String name, 
                             CategoryType type, 
                             LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CategoryType getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


}
