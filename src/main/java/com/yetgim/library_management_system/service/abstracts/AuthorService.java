package com.yetgim.library_management_system.service.abstracts;

import com.yetgim.library_management_system.dto.req.AuthorAddRequest;
import com.yetgim.library_management_system.dto.req.AuthorUpdateRequest;
import com.yetgim.library_management_system.dto.res.AuthorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {

    AuthorResponse createAuthor(AuthorAddRequest request);

    AuthorResponse getAuthorById(Long id);

    Page<AuthorResponse> getAllAuthors(Pageable pageable);

    AuthorResponse updateAuthor(Long id, AuthorUpdateRequest request);

    void deleteAuthor(Long id);
}
