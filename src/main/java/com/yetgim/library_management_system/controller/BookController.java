package com.yetgim.library_management_system.controller;

import com.yetgim.library_management_system.dto.req.BookAddRequest;
import com.yetgim.library_management_system.dto.req.BookUpdateRequest;
import com.yetgim.library_management_system.dto.res.BookResponse;
import com.yetgim.library_management_system.service.abstracts.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;  // ← Now using interface

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookAddRequest request) {
        BookResponse response = bookService.createBook(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        BookResponse response = bookService.getBookById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookResponse> getBookByIsbn(@PathVariable String isbn) {
        BookResponse response = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {
        Page<BookResponse> response = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> searchBooksByTitle(@RequestParam String title) {
        List<BookResponse> response = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BookResponse>> getBooksByAuthor(@PathVariable Long authorId) {
        List<BookResponse> response = bookService.getBooksByAuthor(authorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BookResponse>> getBooksByCategory(@PathVariable Long categoryId) {
        List<BookResponse> response = bookService.getBooksByCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    public ResponseEntity<List<BookResponse>> getBooksWithStock() {
        List<BookResponse> response = bookService.getBooksWithStock();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookUpdateRequest request) {
        BookResponse response = bookService.updateBook(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}