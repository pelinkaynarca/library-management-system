package com.yetgim.library_management_system.service.abstracts;

import com.yetgim.library_management_system.dto.req.CategoryAddRequest;
import com.yetgim.library_management_system.dto.req.CategoryUpdateRequest;
import com.yetgim.library_management_system.dto.res.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryResponse createCategory(CategoryAddRequest request);

    CategoryResponse getCategoryById(Long id);

    Page<CategoryResponse> getAllCategories(Pageable pageable);

    CategoryResponse updateCategory(Long id, CategoryUpdateRequest request);

    void deleteCategory(Long id);
}