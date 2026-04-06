package com.Zorvyn.FinanceApp.service;

import java.util.List;

import com.Zorvyn.FinanceApp.dto.request.CategoryRequest;
import com.Zorvyn.FinanceApp.dto.response.ApiResponse;
import com.Zorvyn.FinanceApp.dto.response.CategoryResponse;
import com.Zorvyn.FinanceApp.entity.Category;
import com.Zorvyn.FinanceApp.enums.CategoryType;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);
    List<CategoryResponse> getAllCategories(String search, CategoryType type);
    CategoryResponse getCategoryById(Long id);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    ApiResponse deleteCategory(Long id);
    CategoryResponse mapToResponse(Category category);

}
