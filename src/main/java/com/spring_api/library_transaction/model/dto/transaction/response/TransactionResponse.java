package com.spring_api.library_transaction.model.dto.transaction.response;

import com.spring_api.library_transaction.model.entity.Transactions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private Long transactionId;
    private Long bookId;
    private String bookTitle;
    private int quantity;
    private String borrowerName;
    private String transactionDate;

    public static TransactionResponse toResponse(Transactions transactions) {
        return new TransactionResponse(
                transactions.getId(),
                transactions.getBook().getId(),
                transactions.getBook().getTitle(),
                transactions.getQuantity(),
                transactions.getBorrowerName(),
                transactions.getCreatedAt().toString()
        );
    }
}
