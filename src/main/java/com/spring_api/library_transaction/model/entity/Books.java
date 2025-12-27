package com.spring_api.library_transaction.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Books extends AuditingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "author", nullable = false, length = 100)
    private String author;

    @Column(name = "published_year")
    private Integer publishedYear;

    @Column(name = "stock", nullable = false)
    private int stock = 1;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Borrows> borrowsList;
}
