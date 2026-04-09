package com.Zorvyn.FinanceApp.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Zorvyn.FinanceApp.dto.request.RecordRequest;
import com.Zorvyn.FinanceApp.dto.response.ApiResponse;
import com.Zorvyn.FinanceApp.dto.response.RecordResponse;
import com.Zorvyn.FinanceApp.enums.CategoryType;
import com.Zorvyn.FinanceApp.service.FinanceRecordService;

@RestController
@RequestMapping("/api/records")
public class FinanceRecordController {

    private static final Logger logger = LoggerFactory.getLogger(FinanceRecordController.class);

    @Autowired
    private FinanceRecordService financeRecordService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<RecordResponse> createRecord(@RequestBody RecordRequest request, Principal principal) {
        // get logged-in user email from JWT
        String email = principal.getName();
        logger.info("Create record request for email: {}", email);
        return ResponseEntity.ok(financeRecordService.createRecord(request, email));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<List<RecordResponse>> getAllRecords(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) CategoryType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Integer limit) {

        logger.info("Get all records request - category: {}, type: {}, from: {}, to: {}, limit: {}",
                categoryName, type, from, to, limit);

        List<RecordResponse> response = financeRecordService.getAllRecords(
                categoryName, type, from, to, limit);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<RecordResponse> updateRecord(
            @PathVariable Long id,
            @RequestBody RecordRequest request) {

        logger.info("Update record request for id: {}", id);

        RecordResponse response = financeRecordService.updateRecord(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<ApiResponse> deleteRecord(
            @PathVariable Long id) {

        logger.info("Delete record request for id: {}", id);

        ApiResponse response = financeRecordService.deleteRecord(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse> restoreRecord(
            @PathVariable Long id) {

        logger.info("Restore record request for id: {}", id);

        ApiResponse response = financeRecordService.restoreRecord(id);
        return ResponseEntity.ok(response);
    }

}
