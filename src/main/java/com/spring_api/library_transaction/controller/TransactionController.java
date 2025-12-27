package com.spring_api.library_transaction.controller;

import com.spring_api.library_transaction.model.dto.DataResponse;
import com.spring_api.library_transaction.model.dto.DatatableResponse;
import com.spring_api.library_transaction.model.dto.transaction.request.CreateTransactionRequest;
import com.spring_api.library_transaction.model.dto.transaction.response.TransactionResponse;
import com.spring_api.library_transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<DataResponse<TransactionResponse>> createTransaction(
            @RequestBody CreateTransactionRequest request
    ){
        DataResponse<TransactionResponse> response = transactionService.createTransaction(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<DatatableResponse<TransactionResponse>> getAllTransactions(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int limit
    ){
        DatatableResponse<TransactionResponse> response = transactionService.getAllTransactions(page, limit);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("{transactionId}")
    public ResponseEntity<DataResponse<TransactionResponse>> getTransactionById(
            @PathVariable("transactionId") Long transactionId
    ) {
        DataResponse<TransactionResponse> response = transactionService.getTransactionById(transactionId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("{transactionId}/quantity")
    public ResponseEntity<DataResponse<TransactionResponse>> updateTransactionQuantity(
            @PathVariable("transactionId") Long transactionId,
            @RequestParam("newQuantity") int newQuantity
    ) {
        DataResponse<TransactionResponse> response = transactionService.updateTransactionQuantity(transactionId, newQuantity);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("{transactionId}/status")
    public ResponseEntity<DataResponse<TransactionResponse>> updateTransactionStatus(
            @PathVariable("transactionId") Long transactionId,
            @RequestParam("status") String status
    ) {
        DataResponse<TransactionResponse> response = transactionService.updateTransactionStatus(transactionId, status);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("{transactionId}")
    public ResponseEntity<DataResponse<TransactionResponse>> deleteTransactionById(
            @PathVariable("transactionId") Long transactionId
    ) {
        DataResponse<TransactionResponse> response = transactionService.deleteTransactionById(transactionId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
