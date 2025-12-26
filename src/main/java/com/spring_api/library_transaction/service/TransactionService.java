package com.spring_api.library_transaction.service;

import com.spring_api.library_transaction.model.dto.DataResponse;
import com.spring_api.library_transaction.model.dto.DatatableResponse;
import com.spring_api.library_transaction.model.dto.transaction.request.CreateTransactionRequest;
import com.spring_api.library_transaction.model.dto.transaction.response.TransactionResponse;

public interface TransactionService {
    DataResponse<TransactionResponse> createTransaction(CreateTransactionRequest request);
    DatatableResponse<TransactionResponse> getAllTransactions(int page, int limit);
    DataResponse<TransactionResponse> getTransactionById(Long transactionId);
    DataResponse<TransactionResponse> updateTransactionQuantity(Long transactionId, int newQuantity);
    DataResponse<TransactionResponse> deleteTransactionById(Long transactionId);
}
