package com.Zorvyn.FinanceApp.service.implement;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Zorvyn.FinanceApp.dto.request.CategoryRequest;
import com.Zorvyn.FinanceApp.dto.response.ApiResponse;
import com.Zorvyn.FinanceApp.dto.response.CategoryResponse;
import com.Zorvyn.FinanceApp.entity.Category;
import com.Zorvyn.FinanceApp.enums.CategoryType;
import com.Zorvyn.FinanceApp.exception.BadRequestException;
import com.Zorvyn.FinanceApp.exception.ResourceNotFoundException;
import com.Zorvyn.FinanceApp.repository.CategoryRepository;
import com.Zorvyn.FinanceApp.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
         try {
            logger.info("Creating category with name: {}", request.getName());

            // check name is not blank
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                throw new BadRequestException("Category name cannot be blank");
            }

            // check type is not null
            if (request.getType() == null) {
                throw new BadRequestException("Category type cannot be blank");
            }
            if (categoryRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new BadRequestException(
                "Category '" + request.getName().trim() + "' already exists"
            );
        }
            Category category = new Category();
            category.setName(request.getName().trim());
            category.setType(request.getType());
            Category savedCategory = categoryRepository.save(category);
            logger.info("Category created successfully with id: {}", savedCategory.getId());
            return mapToResponse(savedCategory);

        }catch(BadRequestException e){
            throw e;
        }catch(Exception e){
            logger.error("Unexpected error creating category: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }
    @Override
    public List<CategoryResponse> getAllCategories(String search, CategoryType type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllCategories'");
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        try {
            logger.info("Fetching category with id: {}", id);

            if (id == null) {
                throw new BadRequestException("Category id cannot be blank");
            }

            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Category not found with id: {}", id);
                        return new ResourceNotFoundException(
                            "Category not found with id: " + id
                        );
                    });

            logger.info("Category fetched successfully with id: {}", id);
            return mapToResponse(category);

        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error fetching category: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }

    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        try {
            logger.info("Updating category with id: {}", id);

            if (id == null) {
                throw new BadRequestException("Category id cannot be blank");
            }

            if ((request.getName() == null || request.getName().trim().isEmpty())
                    && request.getType() == null) {
                throw new BadRequestException(
                    "Provide at least name or type to update"
                );
            }

            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Category not found with id: {}", id);
                        return new ResourceNotFoundException(
                            "Category not found with id: " + id
                        );
                    });
            if (request.getName() != null && !request.getName().trim().isEmpty()) {
                categoryRepository.findByNameIgnoreCase(request.getName().trim())
                        .ifPresent(existing -> {
                            if (!existing.getId().equals(id)) {
                                throw new BadRequestException(
                                    "Category with name '" + request.getName() + "' already exists"
                                );
                            }
                        });
                category.setName(request.getName().trim());
            }

            // update type if provided
            if (request.getType() != null) {
                category.setType(request.getType());
            }
             Category updatedCategory = categoryRepository.save(category);

            logger.info("Category updated successfully with id: {}", id);
            return mapToResponse(updatedCategory);
        }catch(BadRequestException | ResourceNotFoundException e){
            throw e;
        }catch(Exception e){
            logger.error("Unexpected error updating category: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public ApiResponse deleteCategory(Long id) {
        try{
            logger.info("Deleting category with id: {}", id);

            if (id == null) {
                throw new BadRequestException("Category id cannot be blank");
            }

            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Category not found with id: {}", id);
                        return new ResourceNotFoundException(
                            "Category not found with id: " + id
                        );
                    });
             if (category.getRecords() != null && !category.getRecords().isEmpty()) {
                throw new BadRequestException(
                    "Cannot delete category '" + category.getName() +
                    "' because it has " + category.getRecords().size() +
                    " financial records attached to it"
                );
            }

            categoryRepository.delete(category);

            logger.info("Category deleted successfully with id: {}", id);
            return new ApiResponse(true, "Category deleted successfully");
        }catch(BadRequestException | ResourceNotFoundException e){
            throw e;
        }catch(Exception e){
            logger.error("Unexpected error deleting category: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getType(),
            category.getCreatedAt()
        );
    }

}
