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
public class AuthorUpdateRequest {

    @NotBlank(message = "Author name is required")
    @Size(max = 100, message = "Author name cannot exceed 100 characters")
    private String name;

    @Size(max = 5000, message = "Biography cannot exceed 5000 characters")
    private String bio;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;
}