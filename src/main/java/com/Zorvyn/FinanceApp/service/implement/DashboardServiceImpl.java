package com.Zorvyn.FinanceApp.service.implement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Zorvyn.FinanceApp.dto.response.CategoryTotalResponse;
import com.Zorvyn.FinanceApp.dto.response.DashboardSummaryResponse;
import com.Zorvyn.FinanceApp.dto.response.MonthlyTrendResponse;
import com.Zorvyn.FinanceApp.dto.response.RecordResponse;
import com.Zorvyn.FinanceApp.dto.response.WeeklyTrendResponse;
import com.Zorvyn.FinanceApp.entity.Records;
import com.Zorvyn.FinanceApp.enums.CategoryType;
import com.Zorvyn.FinanceApp.exception.IllegalArgumentException;
import com.Zorvyn.FinanceApp.repository.FinancialRecordRepository;
import com.Zorvyn.FinanceApp.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);

    @Autowired
    private FinancialRecordRepository financialRecordRepository;

    @Override
    public DashboardSummaryResponse getSummary() {
        try {
            logger.info("Fetching dashboard summary");
            BigDecimal totalIncome = financialRecordRepository.getTotalIncome();

            // get total expenses from database
            BigDecimal totalExpenses = financialRecordRepository.getTotalExpenses();

            if (totalIncome == null)
                totalIncome = BigDecimal.ZERO;
            if (totalExpenses == null)
                totalExpenses = BigDecimal.ZERO;

            BigDecimal netBalance = totalIncome.subtract(totalExpenses);

            logger.info("Summary - Income: {}, Expenses: {}, Balance: {}",
                    totalIncome, totalExpenses, netBalance);
            return new DashboardSummaryResponse(totalIncome, totalExpenses, netBalance);

        } catch (Exception e) {
            logger.error("Unexpected error fetching summary: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public List<CategoryTotalResponse> getCategoryTotals() {
        try {
            logger.info("Fetching category totals");

            // returns Object[] with [categoryName, categoryType, total]
            List<Object[]> results = financialRecordRepository.findTotalByCategory();

            if (results.isEmpty()) {
                logger.info("No category totals found");
                return List.of();
            }

            // convert Object[] to CategoryTotalResponse
            List<CategoryTotalResponse> response = results.stream()
                    .map(row -> new CategoryTotalResponse(
                            (String) row[0], // category name
                            (CategoryType) row[1], // category type
                            (BigDecimal) row[2] // total amount
                    ))
                    .collect(Collectors.toList());

            logger.info("Found totals for {} categories", response.size());
            return response;

        } catch (Exception e) {
            logger.error("Unexpected error fetching category totals: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public List<RecordResponse> getRecentActivity(Integer limit) {
        try {
            logger.info("Fetching recent activity with limit: {}", limit);

            // default limit to 10 if not provided
            int recordLimit = (limit != null && limit > 0) ? limit : 10;

            // get all active records sorted by date desc
            List<Records> records = financialRecordRepository.findRecentRecords();

            if (records.isEmpty()) {
                logger.info("No recent activity found");
                return List.of();
            }

            // apply limit
            List<RecordResponse> response = records.stream()
                    .limit(recordLimit)
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            logger.info("Returning {} recent records", response.size());
            return response;

        } catch (Exception e) {

            logger.error("Unexpected error fetching Recent activity: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");

        }
    }

    @Override
    public List<MonthlyTrendResponse> getMonthlyTrends(Integer year) {
        try {
            int targetYear = (year != null) ? year : LocalDate.now().getYear();

            logger.info("Fetching monthly trends for year: {}", targetYear);
            List<Object[]> results = financialRecordRepository.findMonthlyTrends(targetYear);

            if (results.isEmpty()) {
                logger.info("No monthly trends found for year: {}", targetYear);
                return List.of();
            }

            List<MonthlyTrendResponse> response = new ArrayList<>();

            for (Object[] row : results) {
                int monthNumber = ((Number) row[0]).intValue();
                BigDecimal income = (BigDecimal) row[1];
                BigDecimal expenses = (BigDecimal) row[2];

                // handle null values
                if (income == null)
                    income = BigDecimal.ZERO;
                if (expenses == null)
                    expenses = BigDecimal.ZERO;

                // calculate net balance for that month
                BigDecimal netBalance = income.subtract(expenses);
                String monthName = Month.of(monthNumber).name().charAt(0) +
                        Month.of(monthNumber).name().substring(1).toLowerCase();

                response.add(new MonthlyTrendResponse(
                        targetYear,
                        monthNumber,
                        monthName,
                        income,
                        expenses,
                        netBalance));
            }

            logger.info("Found trends for {} months", response.size());
            return response;

        } catch (Exception e) {
            logger.error("Unexpected error fetching monthly trends: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public List<WeeklyTrendResponse> getWeeklyTrends(Integer year, Integer month) {
        try {
            int targetYear = (year != null) ? year : LocalDate.now().getYear();
            int targetMonth = (month != null) ? month : LocalDate.now().getMonthValue();

            if (targetMonth < 1 || targetMonth > 12) {
                throw new IllegalArgumentException("Month must be between 1 and 12");
            }

            if (targetYear > LocalDate.now().getYear()) {
                throw new IllegalArgumentException("Year must not be in the future");
            }

            logger.info("Fetching weekly trends for year: {}, month: {}", targetYear, targetMonth);

            List<Object[]> results = financialRecordRepository.findWeeklyTrend(targetYear, targetMonth);

            if (results.isEmpty()) {
                logger.info("No weekly trends found for year: {}, month: {}", targetYear, targetMonth);
                return List.of();
            }

            List<WeeklyTrendResponse> response = new ArrayList<>();

            for (Object[] row : results) {
                int weekNumber = ((Number) row[0]).intValue();
                BigDecimal income = (BigDecimal) row[1];
                BigDecimal expenses = (BigDecimal) row[2];
                LocalDate weekStart = (LocalDate) row[3];
                LocalDate weekEnd = (LocalDate) row[4];

                if (income == null)
                    income = BigDecimal.ZERO;
                if (expenses == null)
                    expenses = BigDecimal.ZERO;

                BigDecimal netBalance = income.subtract(expenses);

                response.add(new WeeklyTrendResponse(
                        weekNumber,
                        weekStart,
                        weekEnd,
                        income,
                        expenses,
                        netBalance));
            }

            logger.info("Found trends for {} weeks", response.size());
            return response;

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid parameter for weekly trends: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error fetching weekly trends: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    public RecordResponse mapToResponse(Records record) {
        return new RecordResponse(
                record.getId(),
                record.getAmount(),
                record.getCategory().getName(),
                record.getCategory().getType(),
                record.getDate(),
                record.getNotes(),
                record.getIsDeleted(),
                record.getCreatedBy().getEmail(),
                record.getCreatedAt());
    }

}
