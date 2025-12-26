package com.spring_api.library_transaction.service.impl;

import com.spring_api.library_transaction.model.dto.DataResponse;
import com.spring_api.library_transaction.model.dto.DatatableResponse;
import com.spring_api.library_transaction.model.dto.PageDataResponse;
import com.spring_api.library_transaction.model.dto.book.request.CreateBookRequest;
import com.spring_api.library_transaction.model.dto.book.response.BookResponse;
import com.spring_api.library_transaction.model.entity.Books;
import com.spring_api.library_transaction.repository.BooksRepository;
import com.spring_api.library_transaction.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.spring_api.library_transaction.model.dto.MessageResponse.*;

@Service
@Slf4j
public class BookServiceImpl implements BookService {
    @Autowired
    private BooksRepository booksRepository;

    @Override
    public DataResponse<BookResponse> createBook(CreateBookRequest request) {
        try {
            Books books = new Books();
            books.setTitle(request.getTitle());
            books.setAuthor(request.getAuthor());
            books.setPublishedYear(request.getPublishedYear());
            books.setStock(request.getStock());
            Books savedBook = booksRepository.save(books);

            BookResponse response = new BookResponse();
            response.setBookId(savedBook.getId());
            response.setTitle(savedBook.getTitle());
            response.setAuthor(savedBook.getAuthor());
            response.setPublishedYear(savedBook.getPublishedYear());
            response.setStock(savedBook.getStock());

            return new DataResponse<>(
                    BOOK_CREATED_SUCCESSFULLY,
                    LocalDateTime.now(),
                    HttpStatus.CREATED.value(),
                    null,
                    null,
                    response
            );
        } catch (Exception e) {
            log.error("Error creating book: {}", e.getMessage());
            return new DataResponse<>(
                    MSG_INTERNAL_SERVER_ERROR,
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ZYD_ERROR_INTERNAL_SERVER,
                    UUID.randomUUID().toString(),
                    null
            );
        }
    }

    @Override
    public DatatableResponse<BookResponse> getAllBooks(int page, int limit) {
        try {
            List<BookResponse> bookResponseList = booksRepository.findAll().stream().map(BookResponse::toResponse).toList();

            Pageable pageable = PageRequest.of(page - 1, limit);

            int total = bookResponseList.size();
            int from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), total);
            int to = Math.min(from + pageable.getPageSize(), total);

            Page<BookResponse> bookResponsePage = new PageImpl<>(
                    bookResponseList.subList(from, to), pageable, total
            );

            PageDataResponse<BookResponse> pageDataResponse = new PageDataResponse<>(
                    page,
                    limit,
                    (int) bookResponsePage.getTotalElements(),
                    bookResponsePage.getTotalPages(),
                    bookResponsePage.getContent()
            );

            if (pageDataResponse.getTotal().equals(0)) {
                return new DatatableResponse<>(
                        EMPTY_DATA,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        ZYD_ERROR_BOOK_NOT_FOUND,
                        UUID.randomUUID().toString(),
                        pageDataResponse
                );
            } else {
                return new DatatableResponse<>(
                        BOOK_SUCCESSFULLY_RETRIEVED,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        null,
                        null,
                        pageDataResponse
                );
            }

        } catch (Exception e) {
            log.error("Error retrieving books: {}", e.getMessage());
            return new DatatableResponse<>(
                    MSG_INTERNAL_SERVER_ERROR,
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ZYD_ERROR_INTERNAL_SERVER,
                    UUID.randomUUID().toString(),
                    null
            );
        }
    }

    @Override
    public DataResponse<BookResponse> getBookById(Long bookId) {
        try {
            Optional<Books> book = booksRepository.findById(bookId);

            if (book.isEmpty()) {
                return new DataResponse<>(
                        BOOK_NOT_FOUND,
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ZYD_ERROR_BOOK_NOT_FOUND,
                        UUID.randomUUID().toString(),
                        null
                );
            } else {
                Books foundBook = book.get();
                BookResponse response = BookResponse.toResponse(foundBook);

                return new DataResponse<>(
                        BOOK_SUCCESSFULLY_RETRIEVED,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        null,
                        null,
                        response
                );
            }

        } catch (Exception e) {
            log.error("Error retrieving book by ID: {}", e.getMessage());
            return new DataResponse<>(
                    MSG_INTERNAL_SERVER_ERROR,
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ZYD_ERROR_INTERNAL_SERVER,
                    UUID.randomUUID().toString(),
                    null
            );
        }
    }

    @Override
    public DataResponse<BookResponse> updateBookStock(Long bookId, int newStock) {
        try {
            Optional<Books> book = booksRepository.findById(bookId);
            if (book.isEmpty()) {
                return new DataResponse<>(
                        BOOK_NOT_FOUND,
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ZYD_ERROR_BOOK_NOT_FOUND,
                        UUID.randomUUID().toString(),
                        null
                );
            } else {
                Books existingBook = book.get();
                existingBook.setStock(newStock);
                Books updatedBook = booksRepository.save(existingBook);

                BookResponse response = BookResponse.toResponse(updatedBook);

                return new DataResponse<>(
                        BOOK_UPDATED_SUCCESSFULLY,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        null,
                        null,
                        response
                );
            }

        } catch (Exception e) {
            log.error("Error updating book stock: {}", e.getMessage());
            return new DataResponse<>(
                    MSG_INTERNAL_SERVER_ERROR,
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ZYD_ERROR_INTERNAL_SERVER,
                    UUID.randomUUID().toString(),
                    null
            );
        }
    }

    @Override
    public DataResponse<BookResponse> deleteBookById(Long bookId) {
        try {
            Optional<Books> book = booksRepository.findById(bookId);
            if (book.isEmpty()) {
                return new DataResponse<>(
                        BOOK_NOT_FOUND,
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ZYD_ERROR_BOOK_NOT_FOUND,
                        UUID.randomUUID().toString(),
                        null
                );
            } else {
                booksRepository.deleteById(bookId);
                return new DataResponse<>(
                        BOOK_DELETED_SUCCESSFULLY,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        null,
                        null,
                        null
                );
            }
        } catch (Exception e) {
            log.error("Error deleting book by ID: {}", e.getMessage());
            return new DataResponse<>(
                    MSG_INTERNAL_SERVER_ERROR,
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ZYD_ERROR_INTERNAL_SERVER,
                    UUID.randomUUID().toString(),
                    null
            );
        }
    }
}
