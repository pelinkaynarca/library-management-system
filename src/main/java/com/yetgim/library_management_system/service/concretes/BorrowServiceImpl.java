package com.yetgim.library_management_system.service.concretes;

import com.yetgim.library_management_system.dto.req.BorrowBookRequest;
import com.yetgim.library_management_system.dto.req.ReturnBookRequest;
import com.yetgim.library_management_system.dto.res.BorrowResponse;
import com.yetgim.library_management_system.entity.Book;
import com.yetgim.library_management_system.entity.Borrow;
import com.yetgim.library_management_system.entity.User;
import com.yetgim.library_management_system.exception.ResourceNotFoundException;
import com.yetgim.library_management_system.mapper.BorrowMapper;
import com.yetgim.library_management_system.repository.BorrowRepository;
import com.yetgim.library_management_system.repository.BookRepository;
import com.yetgim.library_management_system.repository.UserRepository;
import com.yetgim.library_management_system.service.abstracts.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowMapper borrowMapper;

    private static final int MAX_BORROWING_DAYS = 14; // 2 weeks

    @Override
    @Transactional
    public BorrowResponse borrowBook(BorrowBookRequest request) {
        // Validate user exists
        User user = findUserById(request.getUserId());

        // Validate book exists
        Book book = findBookById(request.getBookId());

        // Check if book is available
        if (book.getStock() <= 0) {
            throw new IllegalStateException("Book is not available for borrowing");
        }

        // Check if user already has this book borrowed
        if (borrowRepository.findActiveBorrowByUserAndBook(request.getUserId(), request.getBookId()).isPresent()) {
            throw new IllegalStateException("User already has this book borrowed and not returned");
        }

        Borrow borrow = new Borrow();
        borrow.setUser(user);
        borrow.setBook(book);
        borrow.setBorrowDate(LocalDate.now());
        borrow.setDueDate(LocalDate.now().plusDays(MAX_BORROWING_DAYS));

        Borrow savedBorrow = borrowRepository.save(borrow);

        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        return borrowMapper.toResponse(savedBorrow);
    }

    @Override
    @Transactional
    public BorrowResponse returnBook(ReturnBookRequest request) {
        // Validate user exists
        findUserById(request.getUserId());

        // Validate book exists
        Book book = findBookById(request.getBookId());

        // Find active borrow
        Borrow borrow = borrowRepository.findActiveBorrowByUserAndBook(request.getUserId(), request.getBookId())
                .orElseThrow(() -> new IllegalStateException("No active borrow found for this user and book"));

        // Check if already returned
        if (borrow.getReturnDate() != null) {
            throw new IllegalStateException("Book has already been returned");
        }

        borrow.setReturnDate(LocalDate.now());

        Borrow savedBorrow = borrowRepository.save(borrow);

        book.setStock(book.getStock() + 1);
        bookRepository.save(book);

        return borrowMapper.toResponse(savedBorrow);
    }

    @Override
    public BorrowResponse getBorrowById(Long id) {
        Borrow borrow = findBorrowById(id);
        return borrowMapper.toResponse(borrow);
    }

    @Override
    public List<BorrowResponse> getBorrowsByUser(Long userId) {
        // Validate user exists
        findUserById(userId);

        return borrowRepository.findByUserId(userId)
                .stream()
                .map(borrowMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowResponse> getActiveBorrows() {
        return borrowRepository.findActiveBorrows()
                .stream()
                .map(borrowMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowResponse> getOverdueBorrows() {
        return borrowRepository.findOverdueBorrows(LocalDate.now())
                .stream()
                .map(borrowMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BorrowResponse> getAllBorrows(Pageable pageable) {
        return borrowRepository.findAll(pageable)
                .map(borrowMapper::toResponse);
    }

    // helpers
    private Borrow findBorrowById(Long id) {
        return borrowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow not found with id: " + id));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
    }
}
