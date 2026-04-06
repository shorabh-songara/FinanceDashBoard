package com.Zorvyn.FinanceApp.service;

import java.time.LocalDate;
import java.util.List;

import com.Zorvyn.FinanceApp.dto.request.RecordRequest;
import com.Zorvyn.FinanceApp.dto.response.ApiResponse;
import com.Zorvyn.FinanceApp.dto.response.PagedResponse;
import com.Zorvyn.FinanceApp.dto.response.RecordResponse;
import com.Zorvyn.FinanceApp.entity.Records;
import com.Zorvyn.FinanceApp.enums.CategoryType;

public interface FinanceRecordService {

    RecordResponse createRecord(RecordRequest request, String email);

    List<RecordResponse> getAllRecords(
            String categoryName,
            CategoryType type,
            LocalDate from,
            LocalDate to,
            Integer limit);

    PagedResponse<RecordResponse> getAllRecords(
            String categoryName,
            CategoryType type,
            LocalDate from,
            LocalDate to,
            int page,
            int size);

    RecordResponse updateRecord(Long id, RecordRequest request);

    ApiResponse deleteRecord(Long id);

    ApiResponse restoreRecord(Long id);

    RecordResponse mapToResponse(Records records);

}

