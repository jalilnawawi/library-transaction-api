package com.spring_api.library_transaction.repository;

import com.spring_api.library_transaction.model.entity.Borrows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowsRepository extends JpaRepository<Borrows, Long> {
}
