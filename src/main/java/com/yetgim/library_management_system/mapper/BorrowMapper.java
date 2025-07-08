package com.yetgim.library_management_system.mapper;

import com.yetgim.library_management_system.dto.res.BorrowResponse;
import com.yetgim.library_management_system.entity.Borrow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface BorrowMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    @Mapping(source = "book.isbn", target = "bookIsbn")
    @Mapping(target = "isOverdue", expression = "java(isOverdue(borrow))")
    BorrowResponse toResponse(Borrow borrow);

    default boolean isOverdue(Borrow borrow) {
        return borrow.getReturnDate() == null &&
                borrow.getDueDate().isBefore(LocalDate.now());
    }
}