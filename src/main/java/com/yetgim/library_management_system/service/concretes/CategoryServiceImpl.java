package com.yetgim.library_management_system.service.concretes;

import com.yetgim.library_management_system.dto.req.CategoryAddRequest;
import com.yetgim.library_management_system.dto.req.CategoryUpdateRequest;
import com.yetgim.library_management_system.dto.res.CategoryResponse;
import com.yetgim.library_management_system.entity.Category;
import com.yetgim.library_management_system.exception.ResourceAlreadyExistsException;
import com.yetgim.library_management_system.exception.ResourceNotFoundException;
import com.yetgim.library_management_system.mapper.CategoryMapper;
import com.yetgim.library_management_system.repository.CategoryRepository;
import com.yetgim.library_management_system.service.abstracts.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryAddRequest request) {
        // Check if category name already exists
        if (categoryRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Category with name '" + request.getName() + "' already exists");
        }

        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = findCategoryById(id);
        return categoryMapper.toResponse(category);
    }

    @Override
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponse);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
        Category category = findCategoryById(id);

        // Check if new name already exists
        categoryRepository.findByNameIgnoreCase(request.getName())
                .filter(existingCategory -> !existingCategory.getId().equals(id))
                .ifPresent(existingCategory -> {
                    throw new ResourceAlreadyExistsException("Category with name '" + request.getName() + "' already exists");
                });

        // Update category
        categoryMapper.updateEntity(category, request);
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = findCategoryById(id);

        // Check if category has books
        if (!category.getBooks().isEmpty()) {
            throw new IllegalStateException("Cannot delete category that contains books. Remove books first.");
        }

        categoryRepository.delete(category);
    }

    // helpers
    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}
