package com.Zorvyn.FinanceApp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Zorvyn.FinanceApp.dto.response.CategoryTotalResponse;
import com.Zorvyn.FinanceApp.dto.response.DashboardSummaryResponse;
import com.Zorvyn.FinanceApp.dto.response.MonthlyTrendResponse;
import com.Zorvyn.FinanceApp.dto.response.RecordResponse;
import com.Zorvyn.FinanceApp.dto.response.WeeklyTrendResponse;
import com.Zorvyn.FinanceApp.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
public class DashBoardController {

    private static final Logger logger = LoggerFactory.getLogger(DashBoardController.class);

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<DashboardSummaryResponse> getSummary() {
        logger.info("GET /api/dashboard/summary - Fetching dashboard summary");
        DashboardSummaryResponse response = dashboardService.getSummary();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/CategoryTotal")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<List<CategoryTotalResponse>> getCategoryTotal() {
        logger.info("GET /api/dashboard/CategoryTotal - Fetching category totals");
        List<CategoryTotalResponse> response = dashboardService.getCategoryTotals();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthlyTrend")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<List<MonthlyTrendResponse>> getMonthlyTrend(
            @RequestParam(required = false) Integer year) {
        logger.info("GET /api/dashboard/monthlyTrend - Fetching monthly trends for year: {}", year);
        List<MonthlyTrendResponse> response = dashboardService.getMonthlyTrends(year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/weeklyTrend")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<List<WeeklyTrendResponse>> getWeeklyTrend(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        logger.info("GET /api/dashboard/weeklyTrend - Fetching weekly trends for year: {}, month: {}", year, month);
        List<WeeklyTrendResponse> response = dashboardService.getWeeklyTrends(year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recentActivity")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<List<RecordResponse>> getRecentActivity(
            @RequestParam(required = false) Integer limit) {
        logger.info("GET /api/dashboard/recentActivity - Fetching recent activity with limit: {}", limit);
        List<RecordResponse> response = dashboardService.getRecentActivity(limit);
        return ResponseEntity.ok(response);
    }

}
