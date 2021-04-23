package org.techforum.spring.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.techforum.spring.book.entity.Book;
import org.techforum.spring.book.repository.BookRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book findRandomBook() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false).findAny().get();
    }

    public Book registerBook(@Valid Book book) {
        // TODO
//        IsbnNumbers isbnNumbers = numberProxy.generateIsbnNumbers();
//        book.isbn13 = isbnNumbers.getIsbn13();
//        book.isbn10 = isbnNumbers.getIsbn10();
//
        return bookRepository.save(book);
    }

    // We have no ISBN numbers, we cannot persist in the database
    private Book fallbackPersistBook(Book book) throws FileNotFoundException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        var bookJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(book);
        try (PrintWriter out = new PrintWriter("book-" + Instant.now().toEpochMilli() + ".json")) {
            out.println(bookJson);
        }

        throw new IllegalStateException("Numbers not accessible");
    }

    public List<Book> findAllBooks() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false).collect(toList());
    }

    public long count() {
        return bookRepository.count();
    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    // To be invistgated
    public Book updateBook(@Valid Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }


}
