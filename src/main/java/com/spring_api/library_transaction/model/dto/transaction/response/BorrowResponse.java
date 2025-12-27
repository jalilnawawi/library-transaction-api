package com.spring_api.library_transaction.model.dto.transaction.response;

import com.spring_api.library_transaction.model.entity.Borrows;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowResponse {
    private Long transactionId;
    private Long bookId;
    private String bookTitle;
    private int quantity;
    private String borrowerName;
    private String transactionDate;

    public static BorrowResponse toResponse(Borrows borrows) {
        return new BorrowResponse(
                borrows.getId(),
                borrows.getBook().getId(),
                borrows.getBook().getTitle(),
                borrows.getQuantity(),
                borrows.getUser().getName(),
                borrows.getCreatedAt().toString()
        );
    }
}
