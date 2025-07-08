package com.yetgim.library_management_system.dto.req;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    private LocalDate publishedDate;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @NotNull(message = "Author ID is required")
    @Positive(message = "Author ID must be positive")
    private Long authorId;

    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    private Long categoryId;
}