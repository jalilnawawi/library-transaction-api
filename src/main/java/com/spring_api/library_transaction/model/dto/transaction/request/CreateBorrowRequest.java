package com.spring_api.library_transaction.model.dto.transaction.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBorrowRequest {
    private Long userId;
    private Long bookId;
    private int quantity;
}
