package com.yetgim.library_management_system.mapper;

import com.yetgim.library_management_system.dto.req.BookAddRequest;
import com.yetgim.library_management_system.dto.req.BookUpdateRequest;
import com.yetgim.library_management_system.dto.res.BookResponse;
import com.yetgim.library_management_system.entity.Author;
import com.yetgim.library_management_system.entity.Book;
import com.yetgim.library_management_system.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "borrows", ignore = true)
    Book toEntity(BookAddRequest request, Author author, Category category);

    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "category.id", target = "categoryId")
    BookResponse toResponse(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isbn", ignore = true)
    @Mapping(target = "borrows", ignore = true)
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    void updateEntity(@MappingTarget Book book, BookUpdateRequest request, Author author, Category category);
}