package com.yetgim.library_management_system.controller;

import com.yetgim.library_management_system.dto.req.BorrowBookRequest;
import com.yetgim.library_management_system.dto.req.ReturnBookRequest;
import com.yetgim.library_management_system.dto.res.BorrowResponse;
import com.yetgim.library_management_system.service.abstracts.BorrowService;
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
@RequestMapping("/api/borrows")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping("/borrow")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<BorrowResponse> borrowBook(@Valid @RequestBody BorrowBookRequest request) {
        BorrowResponse response = borrowService.borrowBook(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/return")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<BorrowResponse> returnBook(@Valid @RequestBody ReturnBookRequest request) {
        BorrowResponse response = borrowService.returnBook(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BorrowResponse> getBorrowById(@PathVariable Long id) {
        BorrowResponse response = borrowService.getBorrowById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('MEMBER') and #userId == authentication.principal.id)")
    public ResponseEntity<List<BorrowResponse>> getBorrowsByUser(@PathVariable Long userId) {
        List<BorrowResponse> response = borrowService.getBorrowsByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<BorrowResponse>> getAllBorrows(
            @PageableDefault(size = 20, sort = "borrowDate") Pageable pageable) {
        Page<BorrowResponse> response = borrowService.getAllBorrows(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BorrowResponse>> getActiveBorrows() {
        List<BorrowResponse> response = borrowService.getActiveBorrows();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BorrowResponse>> getOverdueBorrows() {
        List<BorrowResponse> response = borrowService.getOverdueBorrows();
        return ResponseEntity.ok(response);
    }
}
