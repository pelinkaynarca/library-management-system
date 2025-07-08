package com.yetgim.library_management_system.mapper;

import com.yetgim.library_management_system.dto.req.AuthorAddRequest;
import com.yetgim.library_management_system.dto.req.AuthorUpdateRequest;
import com.yetgim.library_management_system.dto.res.AuthorResponse;
import com.yetgim.library_management_system.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    Author toEntity(AuthorAddRequest request);

    AuthorResponse toResponse(Author author);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    void updateEntity(@MappingTarget Author author, AuthorUpdateRequest request);
}