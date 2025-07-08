package com.yetgim.library_management_system.service.abstracts;

import com.yetgim.library_management_system.dto.req.BorrowBookRequest;
import com.yetgim.library_management_system.dto.req.ReturnBookRequest;
import com.yetgim.library_management_system.dto.res.BorrowResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BorrowService {

    BorrowResponse borrowBook(BorrowBookRequest request);

    BorrowResponse returnBook(ReturnBookRequest request);

    BorrowResponse getBorrowById(Long id);

    List<BorrowResponse> getBorrowsByUser(Long userId);

    List<BorrowResponse> getActiveBorrows();

    List<BorrowResponse> getOverdueBorrows();

    Page<BorrowResponse> getAllBorrows(Pageable pageable);
}
