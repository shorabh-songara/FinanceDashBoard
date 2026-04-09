package com.Zorvyn.FinanceApp.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class WeeklyTrendResponse {

    private int weekNumber;
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;

    public WeeklyTrendResponse() {}

    public WeeklyTrendResponse(int weekNumber, LocalDate weekStart, LocalDate weekEnd,
            BigDecimal totalIncome, BigDecimal totalExpenses, BigDecimal netBalance) {
        this.weekNumber = weekNumber;
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netBalance = netBalance;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(LocalDate weekEnd) {
        this.weekEnd = weekEnd;
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
