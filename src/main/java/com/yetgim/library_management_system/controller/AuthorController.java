package com.yetgim.library_management_system.controller;

import com.yetgim.library_management_system.dto.req.AuthorAddRequest;
import com.yetgim.library_management_system.dto.req.AuthorUpdateRequest;
import com.yetgim.library_management_system.dto.res.AuthorResponse;
import com.yetgim.library_management_system.service.abstracts.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody AuthorAddRequest request) {
        AuthorResponse response = authorService.createAuthor(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable Long id) {
        AuthorResponse response = authorService.getAuthorById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<AuthorResponse>> getAllAuthors(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        Page<AuthorResponse> response = authorService.getAllAuthors(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorResponse> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody AuthorUpdateRequest request) {
        AuthorResponse response = authorService.updateAuthor(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}