package com.Zorvyn.FinanceApp.service.implement;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.Zorvyn.FinanceApp.dto.request.RecordRequest;
import com.Zorvyn.FinanceApp.dto.response.ApiResponse;
import com.Zorvyn.FinanceApp.dto.response.PagedResponse;
import com.Zorvyn.FinanceApp.dto.response.RecordResponse;
import com.Zorvyn.FinanceApp.entity.Category;
import com.Zorvyn.FinanceApp.entity.Records;
import com.Zorvyn.FinanceApp.entity.User;
import com.Zorvyn.FinanceApp.enums.CategoryType;
import com.Zorvyn.FinanceApp.exception.BadRequestException;
import com.Zorvyn.FinanceApp.exception.ResourceNotFoundException;
import com.Zorvyn.FinanceApp.repository.CategoryRepository;
import com.Zorvyn.FinanceApp.repository.FinancialRecordRepository;
import com.Zorvyn.FinanceApp.repository.UserRepository;
import com.Zorvyn.FinanceApp.service.FinanceRecordService;

@Service
public class FinanceRecordServiceImpl implements FinanceRecordService {

    private static final Logger logger = LoggerFactory.getLogger(FinanceRecordServiceImpl.class);

    @Autowired
    private FinancialRecordRepository financialRecordRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public RecordResponse createRecord(RecordRequest request, String email) {
        try {
            logger.info("Creating record for user: {}", email);

            // check amount
            if (request.getAmount() == null) {
                throw new BadRequestException("Amount cannot be blank");
            }
            if (request.getAmount().doubleValue() <= 0) {
                throw new BadRequestException("Amount must be greater than 0");
            }

            if (request.getCategoryId() == null) {
                throw new BadRequestException("Category cannot be blank");
            }

            if (request.getDate() == null) {
                throw new BadRequestException("Date cannot be blank");
            }

            if (request.getDate().isBefore(LocalDate.of(2000, 1, 1))) {
                throw new BadRequestException("Date cannot be before year 2000");
            }

            if (request.getDate().isAfter(LocalDate.now())) {
                throw new BadRequestException("Date cannot be in the future");
            }
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> {
                        logger.warn("Category not found with id: {}", request.getCategoryId());
                        return new ResourceNotFoundException(
                                "Category not found with id: " + request.getCategoryId());
                    });

            User createdBy = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.warn("User not found with email: {}", email);
                        return new ResourceNotFoundException("User not found");
                    });
            Records record = new Records();
            record.setAmount(request.getAmount());
            record.setCategory(category);
            record.setDate(request.getDate());
            record.setNotes(request.getNotes());
            record.setCreatedBy(createdBy);
            record.setIsDeleted(false);

            Records savedRecord = financialRecordRepository.save(record);

            logger.info("Record created successfully with id: {}", savedRecord.getId());
            return mapToResponse(savedRecord);

        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error creating record: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public List<RecordResponse> getAllRecords(String categoryName, CategoryType type, LocalDate from, LocalDate to,
            Integer limit) {

        try {
            logger.info("Fetching records - category: {}, type: {}, from: {}, to: {}, limit: {}",
                    categoryName, type, from, to, limit);

            // validate date range if both provided
            if (from != null && to != null && from.isAfter(to)) {
                throw new BadRequestException("From date cannot be after to date");
            }
            List<Records> records = financialRecordRepository.findAllWithFilters(
                    categoryName, type, from, to);

            if (records.isEmpty()) {
                logger.info("No records found");
                return List.of();
            }

            if (limit != null && limit > 0) {
                records = records.stream()
                        .limit(limit)
                        .collect(Collectors.toList());
            }

            logger.info("Found {} records", records.size());

            return records.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error fetching records: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public PagedResponse<RecordResponse> getAllRecords(String categoryName, CategoryType type, LocalDate from,
            LocalDate to, int page, int size) {
        try {
            logger.info("Fetching records (paginated) - category: {}, type: {}, from: {}, to: {}, page: {}, size: {}",
                    categoryName, type, from, to, page, size);

            // validate date range if both provided
            if (from != null && to != null && from.isAfter(to)) {
                throw new BadRequestException("From date cannot be after to date");
            }

            Page<Records> recordPage = financialRecordRepository.findAllWithFilters(
                    categoryName, type, from, to,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date")));

            List<RecordResponse> content = recordPage.getContent().stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            logger.info("Found {} records on page {} of {}", content.size(), page, recordPage.getTotalPages());

            return new PagedResponse<>(
                    content,
                    recordPage.getNumber(),
                    recordPage.getSize(),
                    recordPage.getTotalElements(),
                    recordPage.getTotalPages(),
                    recordPage.isLast());

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error fetching paginated records: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public RecordResponse updateRecord(Long id, RecordRequest request) {
        try {
            logger.info("Updating record with id: {}", id);

            if (id == null) {
                throw new BadRequestException("Record id cannot be blank");
            }

            // check at least one field is provided
            if (request.getAmount() == null &&
                    request.getCategoryId() == null &&
                    request.getDate() == null &&
                    request.getNotes() == null) {
                throw new BadRequestException("Provide at least one field to update");
            }

            Records record = financialRecordRepository.findByIdAndIsDeletedFalse(id)
                    .orElseThrow(() -> {
                        logger.warn("Record not found with id: {}", id);
                        return new ResourceNotFoundException("Record not found with id: " + id);
                    });

            if (request.getAmount() != null) {
                if (request.getAmount().doubleValue() <= 0) {
                    throw new BadRequestException("Amount must be greater than 0");
                }
                record.setAmount(request.getAmount());
            }

            if (request.getCategoryId() != null) {
                Category category = categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() -> {
                            logger.warn("Category not found with id: {}", request.getCategoryId());
                            return new ResourceNotFoundException(
                                    "Category not found with id: " + request.getCategoryId());
                        });
                record.setCategory(category);
            }
            if (request.getDate() != null) {

                if (request.getDate().isAfter(LocalDate.now())) {
                    throw new BadRequestException("Date cannot be in the future");
                }

                if (request.getDate().isBefore(LocalDate.of(2000, 1, 1))) {
                    throw new BadRequestException("Date cannot be before year 2000");
                }

                record.setDate(request.getDate());
            }

            // update notes if provided
            // notes can be set to empty string to clear it
            if (request.getNotes() != null) {
                record.setNotes(request.getNotes().trim());
            }

            Records updatedRecord = financialRecordRepository.save(record);

            logger.info("Record updated successfully with id: {}", id);
            return mapToResponse(updatedRecord);

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating record: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public ApiResponse deleteRecord(Long id) {
        try {
            logger.info("Soft deleting record with id: {}", id);

            if (id == null) {
                throw new BadRequestException("Record id cannot be blank");
            }

            // only find records that are NOT already deleted
            Records record = financialRecordRepository.findByIdAndIsDeletedFalse(id)
                    .orElseThrow(() -> {
                        logger.warn("Record not found or already deleted with id: {}", id);
                        return new ResourceNotFoundException(
                                "Record not found with id: " + id);
                    });

            // flip flag to true
            record.setIsDeleted(true);
            financialRecordRepository.save(record);

            logger.info("Record soft deleted successfully with id: {}", id);
            return new ApiResponse(true, "Record deleted successfully");

        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error deleting record: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public ApiResponse restoreRecord(Long id) {
        try {
            logger.info("Restoring record with id: {}", id);

            if (id == null) {
                throw new BadRequestException("Record id cannot be blank");
            }

            // only find records that ARE deleted
            Records record = financialRecordRepository.findByIdAndIsDeletedTrue(id)
                    .orElseThrow(() -> {
                        logger.warn("Deleted record not found with id: {}", id);
                        return new ResourceNotFoundException(
                                "Deleted record not found with id: " + id);
                    });

            // flip flag back to false
            record.setIsDeleted(false);
            financialRecordRepository.save(record);

            logger.info("Record restored successfully with id: {}", id);
            return new ApiResponse(true, "Record restored successfully");

        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error restoring record: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public RecordResponse mapToResponse(Records records) {
        return new RecordResponse(
                records.getId(),
                records.getAmount(),
                records.getCategory().getName(),
                records.getCategory().getType(),
                records.getDate(),
                records.getNotes(),
                records.getIsDeleted(),
                records.getCreatedBy().getEmail(),
                records.getCreatedAt());
    }

}
