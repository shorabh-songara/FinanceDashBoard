package com.Zorvyn.FinanceApp.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.Zorvyn.FinanceApp.enums.CategoryType;

public class RecordResponse {


    private Long id;
    private BigDecimal amount;
    private String categoryName;
    private CategoryType categoryType;
    private LocalDate date;
    private String notes;
    private Boolean isDeleted;
    private String createdBy;
    private LocalDateTime createdAt;

    
    public RecordResponse(Long id, BigDecimal amount, String categoryName, CategoryType categoryType, LocalDate date,
            String notes, Boolean isDeleted, String createdBy, LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
        this.date = date;
        this.notes = notes;
        this.isDeleted = isDeleted;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public Boolean getIsDeleted() {
        return isDeleted;
    }
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    

}
