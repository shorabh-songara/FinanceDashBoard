package com.Zorvyn.FinanceApp.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RecordRequest {
    
    private BigDecimal amount;
    private Long categoryId;
    private LocalDate date;
    private String notes;

    public RecordRequest(){

    }

    public RecordRequest(BigDecimal amount, Long categoryId, LocalDate date, String notes) {
        this.amount = amount;
        this.categoryId = categoryId;
        this.date = date;
        this.notes = notes;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

}
