package com.yetgim.library_management_system.service.concretes;

import com.yetgim.library_management_system.dto.req.AuthorAddRequest;
import com.yetgim.library_management_system.dto.req.AuthorUpdateRequest;
import com.yetgim.library_management_system.dto.res.AuthorResponse;
import com.yetgim.library_management_system.entity.Author;
import com.yetgim.library_management_system.exception.ResourceAlreadyExistsException;
import com.yetgim.library_management_system.exception.ResourceNotFoundException;
import com.yetgim.library_management_system.mapper.AuthorMapper;
import com.yetgim.library_management_system.repository.AuthorRepository;
import com.yetgim.library_management_system.service.abstracts.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    @Transactional
    public AuthorResponse createAuthor(AuthorAddRequest request) {
        // Check if author name already exists
        if (authorRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Author with name '" + request.getName() + "' already exists");
        }

        Author author = authorMapper.toEntity(request);
        Author savedAuthor = authorRepository.save(author);

        return authorMapper.toResponse(savedAuthor);
    }

    @Override
    public AuthorResponse getAuthorById(Long id) {
        Author author = findAuthorById(id);
        return authorMapper.toResponse(author);
    }

    @Override
    public Page<AuthorResponse> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable)
                .map(authorMapper::toResponse);
    }

    @Override
    @Transactional
    public AuthorResponse updateAuthor(Long id, AuthorUpdateRequest request) {
        Author author = findAuthorById(id);

        // Check if new name already exists
        authorRepository.findByNameIgnoreCase(request.getName())
                .filter(existingAuthor -> !existingAuthor.getId().equals(id))
                .ifPresent(existingAuthor -> {
                    throw new ResourceAlreadyExistsException("Author with name '" + request.getName() + "' already exists");
                });

        authorMapper.updateEntity(author, request);
        Author updatedAuthor = authorRepository.save(author);

        return authorMapper.toResponse(updatedAuthor);
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        Author author = findAuthorById(id);

        // Check if author has books
        if (!author.getBooks().isEmpty()) {
            throw new IllegalStateException("Cannot delete author who has written books. Remove books first.");
        }

        authorRepository.delete(author);
    }

    // helpers
    private Author findAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
    }
}
