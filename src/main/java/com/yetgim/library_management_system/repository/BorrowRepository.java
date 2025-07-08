package com.yetgim.library_management_system.repository;

import com.yetgim.library_management_system.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    @Query("SELECT b FROM Borrow b WHERE b.user.id = :userId AND b.book.id = :bookId AND b.returnDate IS NULL")
    Optional<Borrow> findActiveBorrowByUserAndBook(@Param("userId") Long userId, @Param("bookId") Long bookId);

    List<Borrow> findByUserId(Long userId);

    @Query("SELECT b FROM Borrow b WHERE b.returnDate IS NULL")
    List<Borrow> findActiveBorrows();

    @Query("SELECT b FROM Borrow b WHERE b.returnDate IS NULL AND b.dueDate < :currentDate")
    List<Borrow> findOverdueBorrows(@Param("currentDate") LocalDate currentDate);

    Optional<Borrow> findFirstByUserIdAndBookIdOrderByBorrowDateDesc(Long userId, Long bookId);
}