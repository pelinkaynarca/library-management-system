package com.yetgim.library_management_system.service.abstracts;

import com.yetgim.library_management_system.dto.req.BookAddRequest;
import com.yetgim.library_management_system.dto.req.BookUpdateRequest;
import com.yetgim.library_management_system.dto.res.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {

    BookResponse createBook(BookAddRequest request);

    BookResponse getBookById(Long id);

    BookResponse getBookByIsbn(String isbn);

    Page<BookResponse> getAllBooks(Pageable pageable);

    List<BookResponse> searchBooksByTitle(String title);

    List<BookResponse> getBooksByAuthor(Long authorId);

    List<BookResponse> getBooksByCategory(Long categoryId);

    List<BookResponse> getBooksWithStock();

    BookResponse updateBook(Long id, BookUpdateRequest request);

    void deleteBook(Long id);
}