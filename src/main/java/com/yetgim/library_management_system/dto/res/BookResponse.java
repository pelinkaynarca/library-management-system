package com.yetgim.library_management_system.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

    private Long id;
    private String title;
    private String isbn;
    private LocalDate publishedDate;
    private Integer stock;
    private String authorName;
    private String categoryName;
    private Long authorId;
    private Long categoryId;

}