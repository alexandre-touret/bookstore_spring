package org.techforum.spring.book.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.techforum.spring.book.entity.Book;
import org.techforum.spring.book.service.BookService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController()
@RequestMapping(value = "/api/books", produces = APPLICATION_JSON_VALUE)
public class BookController {
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/random")
    public ResponseEntity<Book> getRandomBook() {
        return ResponseEntity.ok(bookService.findRandomBook());
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAllBooks());
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> count() {
        return ResponseEntity.ok(Map.of("books.count", bookService.count()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@NotNull @PathVariable("id") Long id) {
        return ResponseEntity.of(bookService.findBookById(id));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        return ResponseEntity.accepted().body(bookService.registerBook(book));
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> updateBook(@Valid @RequestBody Book book) {
        return ResponseEntity.ok(bookService.updateBook(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@NotNull @PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.accepted().build();
    }
}
