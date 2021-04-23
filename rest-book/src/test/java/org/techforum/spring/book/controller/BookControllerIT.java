package org.techforum.spring.book.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.techforum.spring.book.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
@Sql("classpath:/books-data.sql")
class BookControllerIT {

    @LocalServerPort
    private int port;

    @Test
    void should_get_a_random_book() {
        var book = restTemplate.getForEntity("http://127.0.0.1:" + port + "/api/books/random", Book.class).getBody();
        assertNotNull(book.getId());
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void should_find_all_books() throws Exception {
        var requestEntity = RequestEntity.get(new URI("http://127.0.0.1:" + port + "/api/books")).accept(MediaType.APPLICATION_JSON).build();
        var books = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<Book>>() {
        }).getBody();
        assertNotNull(books);
        assertEquals(1, books.size());
    }

    @Test
    void should_get_a_count() throws Exception {
        var requestEntity = RequestEntity.get(new URI("http://127.0.0.1:" + port + "/api/books/count")).accept(MediaType.APPLICATION_JSON).build();
        var books = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<Map<String, Long>>() {
        }).getBody();
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals(1, books.get("books.count"));
    }

    @Test
    void should_find_a_book() throws Exception {
        var responseEntity = restTemplate.getForEntity("http://127.0.0.1:" + port + "/api/books/100", Book.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        var book = responseEntity.getBody();
        assertNotNull(book);
        assertEquals(100L, book.getId());
    }

    @Test
    void should_find_no_book() throws Exception {
        var responseEntity = restTemplate.getForEntity("http://127.0.0.1:" + port + "/api/books/999", Book.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertFalse(responseEntity.hasBody());
    }

    @Test
    void should_register_a_book() throws Exception {
        Book book = new Book();
        book.setAuthor("George Orwell");
        book.setTitle("Animal's farm");
        var responseEntity = restTemplate.postForEntity("http://127.0.0.1:" + port + "/api/books", book, Book.class);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        var book1 = responseEntity.getBody();
        assertNotNull(book1);
        assertTrue(book1.getId() >= 1L);
    }

    @Test
    void should_update_book() throws Exception {
        Book book = new Book();
        book.setId(100L);
        book.setAuthor("George Orwell");
        book.setTitle("Animal's farm");
        var responseEntity = restTemplate.exchange("http://127.0.0.1:" + port + "/api/books", HttpMethod.PUT, new HttpEntity<>(book), Book.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void should_delete_book() throws Exception {
        restTemplate.delete("http://127.0.0.1:" + port + "/api/books/100");
        var responseEntity = restTemplate.getForEntity("http://127.0.0.1:" + port + "/api/books/100", Book.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
