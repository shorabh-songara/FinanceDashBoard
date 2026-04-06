package com.Zorvyn.FinanceApp.dto.request;

import com.Zorvyn.FinanceApp.enums.CategoryType;

public class CategoryRequest {

    private String name;
    private CategoryType type;
    
    public CategoryRequest() {}

    public CategoryRequest(String name, CategoryType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public CategoryType getType() { return type; }
    public void setType(CategoryType type) { this.type = type; }

}
