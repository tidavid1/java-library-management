package com.programmers.library_management.repository;

import com.programmers.library_management.domain.Book;

import java.util.Optional;
import java.util.List;

public interface BookRepository {
    void save(Book book);
    Optional<Book> findByBookNumber(int bookNumber);
    List<Book> findByTitle(String searchText);
    List<Book> findAll();
    void delete(Book book);
}