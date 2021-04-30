package org.techforum.spring.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.techforum.spring.book.dto.IsbnNumbers;
import org.techforum.spring.book.entity.Book;
import org.techforum.spring.book.repository.BookRepository;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * Book Spring Service
 */
@Service
public class BookService {

    private BookRepository bookRepository;
    private RestTemplate restTemplate;


    private String isbnServiceURL;

    public BookService(BookRepository bookRepository, RestTemplate restTemplate, @Value("${book.numbers.api.url}") String isbnServiceURL) {
        this.bookRepository = bookRepository;
        this.restTemplate = restTemplate;
        this.isbnServiceURL = isbnServiceURL;
    }

    public Book findRandomBook() {
        List<Long> ids = bookRepository.findAllIds();
        final var size = ids.size();
        var aLong = ids.get(new Random().nextInt(size));
        return findBookById(aLong).get();
    }

    public Book registerBook(@Valid Book book) {
        final var isbnNumbers = restTemplate.getForEntity(isbnServiceURL, IsbnNumbers.class).getBody();
        book.isbn13 = isbnNumbers.getIsbn13();
        book.isbn10 = isbnNumbers.getIsbn10();
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
