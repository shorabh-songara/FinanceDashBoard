package com.Zorvyn.FinanceApp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Zorvyn.FinanceApp.entity.Records;
import com.Zorvyn.FinanceApp.enums.CategoryType;

@Repository
public interface FinancialRecordRepository extends JpaRepository<Records, Long> {

       Optional<Records> findByIdAndIsDeletedFalse(Long id);

       Optional<Records> findByIdAndIsDeletedTrue(Long id);

       @Query("SELECT r FROM Records r " +
                     "JOIN r.category c " +
                     "WHERE r.isDeleted = false " +
                     "AND (:categoryName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))) " +
                     "AND (:type IS NULL OR c.type = :type) " +
                     "AND (:from IS NULL OR r.date >= :from) " +
                     "AND (:to IS NULL OR r.date <= :to) " +
                     "ORDER BY r.date DESC")
       List<Records> findAllWithFilters(
                     @Param("categoryName") String categoryName,
                     @Param("type") CategoryType type,
                     @Param("from") LocalDate from,
                     @Param("to") LocalDate to);

       @Query("SELECT r FROM Records r " +
                     "JOIN r.category c " +
                     "WHERE r.isDeleted = false " +
                     "AND (:categoryName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))) " +
                     "AND (:type IS NULL OR c.type = :type) " +
                     "AND (:from IS NULL OR r.date >= :from) " +
                     "AND (:to IS NULL OR r.date <= :to)")
       Page<Records> findAllWithFilters(
                     @Param("categoryName") String categoryName,
                     @Param("type") CategoryType type,
                     @Param("from") LocalDate from,
                     @Param("to") LocalDate to,
                     Pageable pageable);

       @Query("SELECT COALESCE(SUM(r.amount), 0) FROM Records r " +
                     "JOIN r.category c " +
                     "WHERE r.isDeleted = false " +
                     "AND c.type = 'INCOME'")
       java.math.BigDecimal getTotalIncome();

       @Query("SELECT COALESCE(SUM(r.amount), 0) FROM Records r " +
                     "JOIN r.category c " +
                     "WHERE r.isDeleted = false " +
                     "AND c.type = 'EXPENCE'")
       java.math.BigDecimal getTotalExpenses();

       @Query("SELECT c.name, c.type, COALESCE(SUM(r.amount), 0) " +
                     "FROM Records r " +
                     "JOIN r.category c " +
                     "WHERE r.isDeleted = false " +
                     "GROUP BY c.id, c.name, c.type " +
                     "ORDER BY SUM(r.amount) DESC")
       List<Object[]> findTotalByCategory();

       @Query("SELECT MONTH(r.date), " +
                     "COALESCE(SUM(CASE WHEN c.type = 'INCOME' THEN r.amount ELSE 0 END), 0), " +
                     "COALESCE(SUM(CASE WHEN c.type = 'EXPENCE' THEN r.amount ELSE 0 END), 0) " +
                     "FROM Records r " +
                     "JOIN r.category c " +
                     "WHERE r.isDeleted = false " +
                     "AND YEAR(r.date) = :year " +
                     "GROUP BY MONTH(r.date) " +
                     "ORDER BY MONTH(r.date) ASC")
       List<Object[]> findMonthlyTrends(@Param("year") int year);

       @Query("SELECT r FROM Records r " +
                     "WHERE r.isDeleted = false " +
                     "ORDER BY r.date DESC")
       List<Records> findRecentRecords();

}
