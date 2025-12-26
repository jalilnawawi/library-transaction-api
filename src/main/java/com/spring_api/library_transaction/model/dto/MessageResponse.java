package com.spring_api.library_transaction.model.dto;

import lombok.Getter;

@Getter
public final class MessageResponse {
    public static final String BOOK_CREATED_SUCCESSFULLY = "Book created successfully.";
    public static final String BOOK_UPDATED_SUCCESSFULLY = "Book updated successfully.";
    public static final String BOOK_DELETED_SUCCESSFULLY = "Book deleted successfully.";
    public static final String BOOK_SUCCESSFULLY_RETRIEVED = "Book retrieved successfully.";
    public static final String BOOK_CREATION_FAILED = "Failed to create book.";
    public static final String BOOK_UPDATE_FAILED = "Failed to update book.";
    public static final String BOOK_DELETION_FAILED = "Failed to delete book.";
    public static final String BOOK_RETRIEVAL_FAILED = "Failed to retrieve book.";
    public static final String BOOK_ALREADY_EXISTS = "Book already exists.";
    public static final String BOOK_NOT_FOUND = "Book not found.";
    public static final String BOOK_OUT_OF_STOCK = "Book is out of stock.";

    public static final String TRANSACTION_CREATED_SUCCESSFULLY = "Transaction created successfully.";
    public static final String TRANSACTION_UPDATED_SUCCESSFULLY = "Transaction updated successfully.";
    public static final String TRANSACTION_DELETED_SUCCESSFULLY = "Transaction deleted successfully.";
    public static final String TRANSACTION_SUCCESSFULLY_RETRIEVED = "Transaction retrieved successfully.";
    public static final String TRANSACTION_CREATION_FAILED = "Failed to create transaction.";
    public static final String TRANSACTION_UPDATE_FAILED = "Failed to update transaction.";
    public static final String TRANSACTION_DELETION_FAILED = "Failed to delete transaction.";
    public static final String TRANSACTION_RETRIEVAL_FAILED = "Failed to retrieve transaction.";
    public static final String TRANSACTION_ALREADY_EXISTS = "Transaction already exists.";
    public static final String TRANSACTION_NOT_FOUND = "Transaction not found.";

    public static final String EMPTY_DATA = "No data available.";
    public static final String MSG_INTERNAL_SERVER_ERROR = "Internal server error occurred.";

    public static final String ZYD_ERROR_BOOK_NOT_FOUND = "ZYD-ERR-001";
    public static final String ZYD_ERROR_BOOK_CREATION_FAILED = "ZYD-ERR-002";
    public static final String ZYD_ERROR_BOOK_UPDATED = "ZYD-ERR-003";
    public static final String ZYD_ERROR_BOOK_DELETED = "ZYD-ERR-004";
    public static final String ZYD_ERROR_BOOK_RETRIEVAL_FAILED = "ZYD-ERR-005";
    public static final String ZYD_ERROR_BOOK_ALREADY_EXISTS = "ZYD-ERR-006";
    public static final String ZYD_ERROR_BOOK_OUT_OF_STOCK = "ZYD-ERR-007";
    public static final String ZYD_ERROR_TRANSACTION_NOT_FOUND = "ZYD-ERR-101";
    public static final String ZYD_ERROR_TRANSACTION_CREATION_FAILED = "ZYD-ERR-102";
    public static final String ZYD_ERROR_TRANSACTION_UPDATED = "ZYD-ERR-103";
    public static final String ZYD_ERROR_TRANSACTION_DELETED = "ZYD-ERR-104";
    public static final String ZYD_ERROR_TRANSACTION_RETRIEVAL_FAILED = "ZYD-ERR-105";
    public static final String ZYD_ERROR_TRANSACTION_ALREADY_EXISTS = "ZYD-ERR-106";
    public static final String ZYD_ERROR_EMPTY_DATA = "ZYD-ERR-200";
    public static final String ZYD_ERROR_INTERNAL_SERVER = "ZYD-ERR-500";
}
