package com.Zorvyn.FinanceApp.dto.response;

import java.math.BigDecimal;

public class MonthlyTrendResponse {


    private int year;
    private int month;
    private String monthName;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;

    public MonthlyTrendResponse(){}
    
    public MonthlyTrendResponse(int year, int month, String monthName, BigDecimal totalIncome, BigDecimal totalExpenses,
            BigDecimal netBalance) {
        this.year = year;
        this.month = month;
        this.monthName = monthName;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netBalance = netBalance;
    }
    
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public String getMonthName() {
        return monthName;
    }
    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }
    public BigDecimal getTotalIncome() {
        return totalIncome;
    }
    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }
    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }
    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }
    public BigDecimal getNetBalance() {
        return netBalance;
    }
    public void setNetBalance(BigDecimal netBalance) {
        this.netBalance = netBalance;
    }
    
}
