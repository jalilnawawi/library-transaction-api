package com.spring_api.library_transaction.service.impl;

import com.spring_api.library_transaction.model.dto.DataResponse;
import com.spring_api.library_transaction.model.dto.DatatableResponse;
import com.spring_api.library_transaction.model.dto.PageDataResponse;
import com.spring_api.library_transaction.model.dto.transaction.request.CreateBorrowRequest;
import com.spring_api.library_transaction.model.dto.transaction.response.BorrowResponse;
import com.spring_api.library_transaction.model.entity.Books;
import com.spring_api.library_transaction.model.entity.Borrows;
import com.spring_api.library_transaction.model.entity.Users;
import com.spring_api.library_transaction.model.enums.BorrowStatus;
import com.spring_api.library_transaction.repository.BooksRepository;
import com.spring_api.library_transaction.repository.BorrowsRepository;
import com.spring_api.library_transaction.repository.UserRepository;
import com.spring_api.library_transaction.service.BorrowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.spring_api.library_transaction.model.dto.MessageResponse.*;

@Service
@Slf4j
public class BorrowServiceImpl implements BorrowService {
    @Autowired
    private BorrowsRepository borrowsRepository;

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DataResponse<BorrowResponse> createBorrow(CreateBorrowRequest request) {
        try {
            Optional<Users> user = userRepository.findById(request.getUserId());
            if (user.isEmpty()) {
                return new DataResponse<>(
                        USER_NOT_FOUND,
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ZYD_ERROR_USER_NOT_FOUND,
                        UUID.randomUUID().toString(),
                        null
                );
            }

            if (user.get().getBorrowedBooksCount() <= 0) {
                return new DataResponse<>(
                        USER_BORROW_LIMIT_EXCEEDED,
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ZYD_ERROR_USER_BORROW_LIMIT_EXCEEDED,
                        UUID.randomUUID().toString(),
                        null
                );
            }

            Optional<Books> book = booksRepository.findById(request.getBookId());
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
                if (book.get().getStock() < 1) {
                    return new DataResponse<>(
                            BOOK_OUT_OF_STOCK,
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            ZYD_ERROR_BOOK_OUT_OF_STOCK,
                            UUID.randomUUID().toString(),
                            null
                    );
                }

                Borrows borrows = new Borrows();
                borrows.setBook(book.get());
                borrows.setQuantity(request.getQuantity());
                borrows.setUser(user.get());
                borrows.setStatus(BorrowStatus.OPEN);

                Borrows savedBorrow = borrowsRepository.save(borrows);
                BorrowResponse response = BorrowResponse.toResponse(savedBorrow);

                Books updateBookStock = book.get();
                updateBookStock.setStock(updateBookStock.getStock() - request.getQuantity());
                booksRepository.save(updateBookStock);

                Users updateUserBorrowedCount = user.get();
                updateUserBorrowedCount.setBorrowedBooksCount(updateUserBorrowedCount.getBorrowedBooksCount() - 1);
                userRepository.save(updateUserBorrowedCount);

                return new DataResponse<>(
                        TRANSACTION_CREATED_SUCCESSFULLY,
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        null,
                        null,
                        response
                );
            }

        } catch (Exception e) {
            log.error("Error creating borrow: {}", e.getMessage());
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
    public DatatableResponse<BorrowResponse> getAllBorrows(int page, int limit) {
        try {
            List<Borrows> borrowsList = borrowsRepository.findAll();

            List<BorrowResponse> borrowResponseList = borrowsList.stream()
                    .map(BorrowResponse::toResponse)
                    .toList();

            Pageable pageable = PageRequest.of(page - 1, limit);

            int total = borrowResponseList.size();
            int from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), total);
            int to = Math.min(from + pageable.getPageSize(), total);

            Page<BorrowResponse> borrowResponsePage = new PageImpl<>(
                    borrowResponseList.subList(from, to), pageable, total
            );

            PageDataResponse<BorrowResponse> pageDataResponse = new PageDataResponse<>(
                    page,
                    limit,
                    (int) borrowResponsePage.getTotalElements(),
                    borrowResponsePage.getTotalPages(),
                    borrowResponsePage.getContent()
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
            log.error("Error retrieving borrows: {}", e.getMessage());
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
    public DataResponse<BorrowResponse> getBorrowById(Long borrowId) {
        try {
            Optional<Borrows> existingBorrow = borrowsRepository.findById(borrowId);
            if (existingBorrow.isEmpty()) {
                return new DataResponse<>(
                        TRANSACTION_NOT_FOUND,
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ZYD_ERROR_TRANSACTION_NOT_FOUND,
                        UUID.randomUUID().toString(),
                        null
                );
            } else {
                BorrowResponse response = BorrowResponse.toResponse(existingBorrow.get());
                return new DataResponse<>(
                        TRANSACTION_SUCCESSFULLY_RETRIEVED,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        null,
                        null,
                        response
                );
            }

        } catch (Exception e) {
            log.error("Error retrieving borrow by ID: {}", e.getMessage());
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
    public DataResponse<BorrowResponse> updateBorrowQuantity(Long borrowId, int newQuantity) {
        try {
            Optional<Borrows> existingBorrow = borrowsRepository.findById(borrowId);
            if (existingBorrow.isEmpty()) {
                return new DataResponse<>(
                        TRANSACTION_NOT_FOUND,
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ZYD_ERROR_TRANSACTION_NOT_FOUND,
                        UUID.randomUUID().toString(),
                        null
                );
            }

            Optional<Books> book = booksRepository.findById(existingBorrow.get().getBook().getId());

            // validasi stock buku, apabila pengurangan stock melebihi stock yang ada
            if (book.isPresent() && book.get().getStock() - newQuantity < 1) {
                return new DataResponse<>(
                        BOOK_OUT_OF_STOCK,
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ZYD_ERROR_BOOK_OUT_OF_STOCK,
                        UUID.randomUUID().toString(),
                        null
                );
            }

            Borrows borrowToUpdate = existingBorrow.get();
            borrowToUpdate.setQuantity(newQuantity);
            Borrows updatedBorrow = borrowsRepository.save(borrowToUpdate);
            BorrowResponse response = BorrowResponse.toResponse(updatedBorrow);

            Books updateBookStock = book.get();
            updateBookStock.setStock(updateBookStock.getStock() - newQuantity);
            booksRepository.save(updateBookStock);

            return new DataResponse<>(
                    TRANSACTION_UPDATED_SUCCESSFULLY,
                    LocalDateTime.now(),
                    HttpStatus.OK.value(),
                    null,
                    null,
                    response
            );

        } catch (Exception e) {
            log.error("Error updating borrow quantity: {}", e.getMessage());
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
    public DataResponse<BorrowResponse> updateBorrowStatus(Long borrowId, String status) {
        try {
            Optional<Borrows> existingBorrow = borrowsRepository.findById(borrowId);
            if (existingBorrow.isEmpty()) {
                return new DataResponse<>(
                        TRANSACTION_NOT_FOUND,
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ZYD_ERROR_TRANSACTION_NOT_FOUND,
                        UUID.randomUUID().toString(),
                        null
                );
            } else {
                Borrows borrowToUpdate = existingBorrow.get();
                if (status.equalsIgnoreCase("overdue")) {
                    borrowToUpdate.setStatus(BorrowStatus.OVERDUE);
                } else if (status.equalsIgnoreCase("closed")) {
                    borrowToUpdate.setStatus(BorrowStatus.CLOSED);
                } else {
                    borrowToUpdate.setStatus(BorrowStatus.OPEN);
                }

                Borrows updatedBorrow = borrowsRepository.save(borrowToUpdate);
                BorrowResponse response = BorrowResponse.toResponse(updatedBorrow);

                return new DataResponse<>(
                        TRANSACTION_UPDATED_SUCCESSFULLY,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        null,
                        null,
                        response
                );
            }

        } catch (Exception e) {
            log.error("Error updating borrow status: {}", e.getMessage());
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
    public DataResponse<BorrowResponse> deleteBorrowById(Long borrowId) {
        try {
            Optional<Borrows> existingBorrow = borrowsRepository.findById(borrowId);
            if (existingBorrow.isEmpty()) {
                return new DataResponse<>(
                        TRANSACTION_NOT_FOUND,
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ZYD_ERROR_TRANSACTION_NOT_FOUND,
                        UUID.randomUUID().toString(),
                        null
                );
            } else {
                borrowsRepository.deleteById(borrowId);
                return new DataResponse<>(
                        TRANSACTION_DELETED_SUCCESSFULLY,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        null,
                        null,
                        null
                );
            }
        } catch (Exception e) {
            log.error("Error deleting borrow by ID: {}", e.getMessage());
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
