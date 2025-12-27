package com.spring_api.library_transaction.service;

import com.spring_api.library_transaction.model.dto.DataResponse;
import com.spring_api.library_transaction.model.dto.DatatableResponse;
import com.spring_api.library_transaction.model.dto.transaction.request.CreateBorrowRequest;
import com.spring_api.library_transaction.model.dto.transaction.response.BorrowResponse;

public interface BorrowService {
    DataResponse<BorrowResponse> createBorrow(CreateBorrowRequest request);
    DatatableResponse<BorrowResponse> getAllBorrows(int page, int limit);
    DataResponse<BorrowResponse> getBorrowById(Long borrowId);
    DataResponse<BorrowResponse> updateBorrowQuantity(Long borrowId, int newQuantity);
    DataResponse<BorrowResponse> updateBorrowStatus(Long borrowId, String status);
    DataResponse<BorrowResponse> deleteBorrowById(Long borrowId);
}
