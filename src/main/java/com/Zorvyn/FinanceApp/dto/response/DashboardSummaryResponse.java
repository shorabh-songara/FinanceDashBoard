package com.Zorvyn.FinanceApp.dto.response;

import java.math.BigDecimal;

public class DashboardSummaryResponse {

    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;
    
    public DashboardSummaryResponse(){}
    public DashboardSummaryResponse(BigDecimal totalIncome, BigDecimal totalExpenses, BigDecimal netBalance) {
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netBalance = netBalance;
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
