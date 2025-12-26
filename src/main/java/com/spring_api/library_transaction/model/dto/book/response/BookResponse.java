package com.spring_api.library_transaction.model.dto.book.response;

import com.spring_api.library_transaction.model.entity.Books;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Long bookId;
    private String title;
    private String author;
    private Integer publishedYear;
    private int stock;

    public static BookResponse toResponse(Books book) {
        return new BookResponse(
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            book.getPublishedYear(),
            book.getStock()
        );
    }
}
