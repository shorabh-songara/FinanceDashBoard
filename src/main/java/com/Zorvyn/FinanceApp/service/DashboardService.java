package com.Zorvyn.FinanceApp.service;

import java.util.List;

import com.Zorvyn.FinanceApp.dto.response.CategoryTotalResponse;
import com.Zorvyn.FinanceApp.dto.response.DashboardSummaryResponse;
import com.Zorvyn.FinanceApp.dto.response.MonthlyTrendResponse;
import com.Zorvyn.FinanceApp.dto.response.RecordResponse;

public interface DashboardService {
    DashboardSummaryResponse getSummary();

    List<CategoryTotalResponse> getCategoryTotals();

    List<RecordResponse> getRecentActivity(Integer limit);

    List<MonthlyTrendResponse> getMonthlyTrends(Integer year);
}
