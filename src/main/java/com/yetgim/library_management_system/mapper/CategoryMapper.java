package com.yetgim.library_management_system.mapper;

import com.yetgim.library_management_system.dto.req.CategoryAddRequest;
import com.yetgim.library_management_system.dto.req.CategoryUpdateRequest;
import com.yetgim.library_management_system.dto.res.CategoryResponse;
import com.yetgim.library_management_system.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    Category toEntity(CategoryAddRequest request);

    CategoryResponse toResponse(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    void updateEntity(@MappingTarget Category category, CategoryUpdateRequest request);
}