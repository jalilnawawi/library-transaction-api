package com.spring_api.library_transaction.controller;

import com.spring_api.library_transaction.model.dto.DataResponse;
import com.spring_api.library_transaction.model.dto.DatatableResponse;
import com.spring_api.library_transaction.model.dto.book.request.CreateBookRequest;
import com.spring_api.library_transaction.model.dto.book.response.BookResponse;
import com.spring_api.library_transaction.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/books")
public class BookController {
    @Autowired
    BookService bookService;

    @PostMapping
    public ResponseEntity<DataResponse<BookResponse>> createBook(@RequestBody CreateBookRequest request){
        DataResponse<BookResponse> response = bookService.createBook(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<DatatableResponse<BookResponse>> getAllBooks(
        @RequestParam(value = "page", defaultValue = "1")  int page,
        @RequestParam(value = "limit", defaultValue = "10") int limit,
        @RequestParam(value = "sortField", defaultValue = "id") String sortField,
        @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder
    ){
        DatatableResponse<BookResponse> response = bookService.getAllBooks(page, limit);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("{bookId}")
    public ResponseEntity<DataResponse<BookResponse>> getBookById(@PathVariable("bookId") Long bookId){
        DataResponse<BookResponse> response = bookService.getBookById(bookId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("{bookId}/stock")
    public ResponseEntity<DataResponse<BookResponse>> updateBookStock(
        @PathVariable("bookId") Long bookId,
        @RequestParam("newStock") int newStock
    ) {
        DataResponse<BookResponse> response = bookService.updateBookStock(bookId, newStock);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<DataResponse<BookResponse>> deleteBookById(@PathVariable("bookId") Long bookId) {
        DataResponse<BookResponse> response = bookService.deleteBookById(bookId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

