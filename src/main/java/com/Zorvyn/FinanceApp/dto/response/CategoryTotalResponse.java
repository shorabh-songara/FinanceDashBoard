package com.Zorvyn.FinanceApp.dto.response;

import java.math.BigDecimal;

import com.Zorvyn.FinanceApp.enums.CategoryType;

public class CategoryTotalResponse {
    private String categoryName;
    private CategoryType categoryType;
    private BigDecimal total;

    public CategoryTotalResponse(){
        
    }
    
    public CategoryTotalResponse(String categoryName, CategoryType categoryType, BigDecimal total) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
        this.total = total;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public CategoryType getCategoryType() {
        return categoryType;
    }
    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }
    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}
