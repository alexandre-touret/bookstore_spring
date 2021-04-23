package org.techforum.spring.book.service;

import org.assertj.core.api.Assertions;
import org.techforum.spring.book.entity.Book;
import org.techforum.spring.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BookServiceTest {

    @MockBean
    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(bookRepository);
    }

    @Test
    void should_find_a_random_book() {
        when(bookRepository.findAll()).thenReturn(createBookList());
        assertNotNull(bookService.findRandomBook());
    }

    @Test
    void should_register_a_book() {
        var book = new Book();
        book.setId(1L);
        book.setAuthor("author");
        book.setDescription("description");
        book.setPrice(BigDecimal.TEN);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        var registerBook = bookService.registerBook(book);
        assertNotNull(registerBook);
        assertEquals(book.getId(), registerBook.getId());
    }

    @Test
    void should_find_all_books() {
        List<Book> books = createBookList();
        when(bookRepository.findAll()).thenReturn(books);

        var allBooks = bookService.findAllBooks();
        assertNotNull(allBooks);
        assertEquals(2, allBooks.size());
        assertTrue(allBooks.containsAll(books));
    }

    private List<Book> createBookList() {
        var book = new Book();
        book.setId(1L);
        var book2 = new Book();
        book2.setId(2L);
        return Arrays.asList(book, book2);
    }

    @Test
    void should_get_count() {
        when(bookRepository.count()).thenReturn(2L);
        assertEquals(2L, bookService.count());
    }

    @Test
    void should_find_books_by_id() {
        Book book = new Book();
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        var bookById = bookService.findBookById(1L);
        assertEquals(1L, bookById.orElseThrow().getId());
    }

    @Test
    void should_update_book() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Harriet Beecher Stowe");
        when(bookRepository.save(book)).thenReturn(book);
        var updateBook = bookService.updateBook(book);
        assertNotNull(updateBook);
        assertEquals(book.getAuthor(), updateBook.getAuthor());
    }

    @Test
    void should_delete_book() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Harriet Beecher Stowe");

        doNothing().when(bookRepository).deleteById(1L);
        bookService.deleteBook(1L);
    }


}
