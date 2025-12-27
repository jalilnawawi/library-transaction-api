package com.spring_api.library_transaction.service.impl;

import com.spring_api.library_transaction.model.dto.DataResponse;
import com.spring_api.library_transaction.model.dto.DatatableResponse;
import com.spring_api.library_transaction.model.dto.PageDataResponse;
import com.spring_api.library_transaction.model.dto.transaction.request.CreateTransactionRequest;
import com.spring_api.library_transaction.model.dto.transaction.response.TransactionResponse;
import com.spring_api.library_transaction.model.entity.Books;
import com.spring_api.library_transaction.model.entity.Transactions;
import com.spring_api.library_transaction.model.entity.Users;
import com.spring_api.library_transaction.model.enums.TransactionStatus;
import com.spring_api.library_transaction.repository.BooksRepository;
import com.spring_api.library_transaction.repository.TransactionsRepository;
import com.spring_api.library_transaction.repository.UserRepository;
import com.spring_api.library_transaction.service.TransactionService;
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
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DataResponse<TransactionResponse> createTransaction(CreateTransactionRequest request) {
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

                Transactions transactions = new Transactions();
                transactions.setBook(book.get());
                transactions.setQuantity(request.getQuantity());
                transactions.setUser(user.get());
                transactions.setStatus(TransactionStatus.OPEN);

                Transactions savedTransaction = transactionsRepository.save(transactions);
                TransactionResponse response = TransactionResponse.toResponse(savedTransaction);

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
            log.error("Error creating transaction: {}", e.getMessage());
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
    public DatatableResponse<TransactionResponse> getAllTransactions(int page, int limit) {
        try {
            List<Transactions> transactionsList = transactionsRepository.findAll();

            List<TransactionResponse> transactionResponseList = transactionsList.stream()
                    .map(TransactionResponse::toResponse)
                    .toList();

            Pageable pageable = PageRequest.of(page - 1, limit);

            int total = transactionResponseList.size();
            int from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), total);
            int to = Math.min(from + pageable.getPageSize(), total);

            Page<TransactionResponse> transactionResponsePage = new PageImpl<>(
                    transactionResponseList.subList(from, to), pageable, total
            );

            PageDataResponse<TransactionResponse> pageDataResponse = new PageDataResponse<>(
                    page,
                    limit,
                    (int) transactionResponsePage.getTotalElements(),
                    transactionResponsePage.getTotalPages(),
                    transactionResponsePage.getContent()
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
            log.error("Error retrieving transactions: {}", e.getMessage());
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
    public DataResponse<TransactionResponse> getTransactionById(Long transactionId) {
        try {
            Optional<Transactions> existingTransaction = transactionsRepository.findById(transactionId);
            if (existingTransaction.isEmpty()) {
                return new DataResponse<>(
                        TRANSACTION_NOT_FOUND,
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ZYD_ERROR_TRANSACTION_NOT_FOUND,
                        UUID.randomUUID().toString(),
                        null
                );
            } else {
                TransactionResponse response = TransactionResponse.toResponse(existingTransaction.get());
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
            log.error("Error retrieving transaction by ID: {}", e.getMessage());
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
    public DataResponse<TransactionResponse> updateTransactionQuantity(Long transactionId, int newQuantity) {
        try {
            Optional<Transactions> existingTransaction = transactionsRepository.findById(transactionId);
            if (existingTransaction.isEmpty()) {
                return new DataResponse<>(
                        TRANSACTION_NOT_FOUND,
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ZYD_ERROR_TRANSACTION_NOT_FOUND,
                        UUID.randomUUID().toString(),
                        null
                );
            }

            Optional<Books> book = booksRepository.findById(existingTransaction.get().getBook().getId());

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

            Transactions transactionToUpdate = existingTransaction.get();
            transactionToUpdate.setQuantity(newQuantity);
            Transactions updatedTransaction = transactionsRepository.save(transactionToUpdate);
            TransactionResponse response = TransactionResponse.toResponse(updatedTransaction);

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
            log.error("Error updating transaction quantity: {}", e.getMessage());
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
    public DataResponse<TransactionResponse> updateTransactionStatus(Long transactionId, String status) {
        try {
            Optional<Transactions> existingTransaction = transactionsRepository.findById(transactionId);
            if (existingTransaction.isEmpty()) {
                return new DataResponse<>(
                        TRANSACTION_NOT_FOUND,
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ZYD_ERROR_TRANSACTION_NOT_FOUND,
                        UUID.randomUUID().toString(),
                        null
                );
            } else {
                Transactions transactionToUpdate = existingTransaction.get();
                if (status.equalsIgnoreCase("overdue")) {
                    transactionToUpdate.setStatus(TransactionStatus.OVERDUE);
                } else if (status.equalsIgnoreCase("closed")) {
                    transactionToUpdate.setStatus(TransactionStatus.CLOSED);
                } else {
                    transactionToUpdate.setStatus(TransactionStatus.OPEN);
                }

                Transactions updatedTransaction = transactionsRepository.save(transactionToUpdate);
                TransactionResponse response = TransactionResponse.toResponse(updatedTransaction);

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
            log.error("Error updating transaction status: {}", e.getMessage());
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
    public DataResponse<TransactionResponse> deleteTransactionById(Long transactionId) {
        try {
            Optional<Transactions> existingTransaction = transactionsRepository.findById(transactionId);
            if (existingTransaction.isEmpty()) {
                return new DataResponse<>(
                        TRANSACTION_NOT_FOUND,
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        ZYD_ERROR_TRANSACTION_NOT_FOUND,
                        UUID.randomUUID().toString(),
                        null
                );
            } else {
                transactionsRepository.deleteById(transactionId);
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
            log.error("Error deleting transaction by ID: {}", e.getMessage());
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
