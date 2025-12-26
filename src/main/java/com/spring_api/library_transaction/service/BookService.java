package com.spring_api.library_transaction.service;

import com.spring_api.library_transaction.model.dto.DataResponse;
import com.spring_api.library_transaction.model.dto.DatatableResponse;
import com.spring_api.library_transaction.model.dto.book.request.CreateBookRequest;
import com.spring_api.library_transaction.model.dto.book.response.BookResponse;

public interface BookService {
    DataResponse<BookResponse> createBook(CreateBookRequest request);
    DatatableResponse<BookResponse> getAllBooks(int page, int limit);
    DataResponse<BookResponse> getBookById(Long bookId);
    DataResponse<BookResponse> updateBookStock(Long bookId, int additionalStock);
    DataResponse<BookResponse> deleteBookById(Long bookId);
}
