package com.spring_api.library_transaction.controller;

import com.spring_api.library_transaction.model.dto.DataResponse;
import com.spring_api.library_transaction.model.dto.DatatableResponse;
import com.spring_api.library_transaction.model.dto.transaction.request.CreateBorrowRequest;
import com.spring_api.library_transaction.model.dto.transaction.response.BorrowResponse;
import com.spring_api.library_transaction.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/borrow")
public class BorrowController {
    @Autowired
    private BorrowService borrowService;

    @PostMapping
    public ResponseEntity<DataResponse<BorrowResponse>> createBorrow(
            @RequestBody CreateBorrowRequest request
    ){
        DataResponse<BorrowResponse> response = borrowService.createBorrow(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<DatatableResponse<BorrowResponse>> getAllBorrows(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int limit
    ){
        DatatableResponse<BorrowResponse> response = borrowService.getAllBorrows(page, limit);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("{borrowId}")
    public ResponseEntity<DataResponse<BorrowResponse>> getBorrowById(
            @PathVariable("borrowId") Long borrowId
    ) {
        DataResponse<BorrowResponse> response = borrowService.getBorrowById(borrowId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("{borrowId}/quantity")
    public ResponseEntity<DataResponse<BorrowResponse>> updateBorrowQuantity(
            @PathVariable("borrowId") Long borrowId,
            @RequestParam("newQuantity") int newQuantity
    ) {
        DataResponse<BorrowResponse> response = borrowService.updateBorrowQuantity(borrowId, newQuantity);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("{borrowId}/status")
    public ResponseEntity<DataResponse<BorrowResponse>> updateBorrowStatus(
            @PathVariable("borrowId") Long borrowId,
            @RequestParam("status") String status
    ) {
        DataResponse<BorrowResponse> response = borrowService.updateBorrowStatus(borrowId, status);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("{borrowId}")
    public ResponseEntity<DataResponse<BorrowResponse>> deleteBorrowById(
            @PathVariable("borrowId") Long borrowId
    ) {
        DataResponse<BorrowResponse> response = borrowService.deleteBorrowById(borrowId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
