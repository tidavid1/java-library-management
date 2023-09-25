package com.programmers.library_management.service;

import com.programmers.library_management.domain.Book;
import com.programmers.library_management.domain.Status;
import com.programmers.library_management.exception.*;
import com.programmers.library_management.repository.BookRepository;
import com.programmers.library_management.repository.ProductBookRepository;
import com.programmers.library_management.repository.TestBookRepository;

import java.util.List;

public class LibraryManagementService {
    private final BookRepository bookRepository;
    public LibraryManagementService(boolean isTest){
        this.bookRepository = isTest ? new TestBookRepository() : new ProductBookRepository();
    }

    public void addBook(String title, String writer, String pageNumber){
        Book newBook = new Book(bookRepository.generateBookNumber(), title, writer, Integer.parseInt(pageNumber));
        for(Book book:bookRepository.findByTitle(title)){
            if(book.equals(newBook)){
                throw new CBookAlreadyExistException();
            }
        }
        bookRepository.save(newBook);
    }

    public void rantBook(int bookNumber){
        Book book = bookRepository.findByBookNumber(bookNumber).orElseThrow(CBookNumberNotExistException::new);
        switch (book.getStatus()){
            case Ranted -> throw new CBookAlreadyRantedException();
            case Lost -> throw new CBookAlreadyLostException();
            case Organized -> {
                if (!book.isOrganized()){
                    throw new CBookInOrganizeException();
                }
            }
        }
        book.rant();
        bookRepository.save(book);
    }

    public void returnBook(int bookNumber){
        Book book = bookRepository.findByBookNumber(bookNumber).orElseThrow(CBookNumberNotExistException::new);
        switch (book.getStatus()){
            case Organized, Available -> throw new CBookAlreadyReturnedException();
        }
        book.returned();
        bookRepository.save(book);
    }

    public void lostBook(int bookNumber){
        Book book = bookRepository.findByBookNumber(bookNumber).orElseThrow(CBookNumberNotExistException::new);
        if(book.getStatus().equals(Status.Lost)){
            throw new CBookAlreadyLostException();
        }
        book.lost();
        bookRepository.save(book);
    }

    public void deleteBook(int bookNumber){
        Book book = bookRepository.findByBookNumber(bookNumber).orElseThrow(CBookNumberNotExistException::new);
        bookRepository.delete(book);
    }

    public List<Book> showAllBooks(){
        return bookRepository.findAll();
    }

    public List<Book> searchBook(String text){
        return bookRepository.findByTitle(text);
    }

}