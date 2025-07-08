package com.yetgim.library_management_system.service.concretes;

import com.yetgim.library_management_system.dto.req.BookAddRequest;
import com.yetgim.library_management_system.dto.req.BookUpdateRequest;
import com.yetgim.library_management_system.dto.res.BookResponse;
import com.yetgim.library_management_system.entity.Author;
import com.yetgim.library_management_system.entity.Book;
import com.yetgim.library_management_system.entity.Category;
import com.yetgim.library_management_system.exception.ResourceAlreadyExistsException;
import com.yetgim.library_management_system.exception.ResourceNotFoundException;
import com.yetgim.library_management_system.mapper.BookMapper;
import com.yetgim.library_management_system.repository.AuthorRepository;
import com.yetgim.library_management_system.repository.BookRepository;
import com.yetgim.library_management_system.repository.CategoryRepository;
import com.yetgim.library_management_system.service.abstracts.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookResponse createBook(BookAddRequest request) {
        // Check if ISBN already exists
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new ResourceAlreadyExistsException("Book with ISBN " + request.getIsbn() + " already exists");
        }

        Author author = findAuthorById(request.getAuthorId());

        Category category = findCategoryById(request.getCategoryId());

        Book book = bookMapper.toEntity(request, author, category);
        Book savedBook = bookRepository.save(book);

        return bookMapper.toResponse(savedBook);
    }

    @Override
    public BookResponse getBookById(Long id) {
        Book book = findBookById(id);
        return bookMapper.toResponse(book);
    }

    @Override
    public BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ISBN: " + isbn));
        return bookMapper.toResponse(book);
    }

    @Override
    public Page<BookResponse> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toResponse);
    }

    @Override
    public List<BookResponse> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getBooksByAuthor(Long authorId) {
        // Validate author exists
        findAuthorById(authorId);

        return bookRepository.findByAuthorId(authorId)
                .stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getBooksByCategory(Long categoryId) {
        // Validate category exists
        findCategoryById(categoryId);

        return bookRepository.findByCategoryId(categoryId)
                .stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getBooksWithStock() {
        return bookRepository.findBooksWithStock()
                .stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookResponse updateBook(Long id, BookUpdateRequest request) {
        Book book = findBookById(id);

        // Validate author exists
        Author author = findAuthorById(request.getAuthorId());

        // Validate category exists
        Category category = findCategoryById(request.getCategoryId());

        // Update book
        bookMapper.updateEntity(book, request, author, category);
        Book updatedBook = bookRepository.save(book);

        return bookMapper.toResponse(updatedBook);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = findBookById(id);

        // Check if book has active borrows
        boolean hasActiveBorrows = book.getBorrows().stream()
                .anyMatch(borrow -> borrow.getReturnDate() == null);

        if (hasActiveBorrows) {
            throw new IllegalStateException("Cannot delete book with active borrows");
        }

        bookRepository.delete(book);
    }

    // helper
    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    private Author findAuthorById(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }
}