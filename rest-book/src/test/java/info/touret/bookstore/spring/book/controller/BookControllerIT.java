package info.touret.bookstore.spring.book.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.touret.bookstore.spring.book.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import info.touret.bookstore.spring.book.dto.IsbnNumbers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql("classpath:/books-data.sql")
class BookControllerIT {

    public static final String API_BOOKS = "http://127.0.0.1:8081/api/books";
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockServer.reset();
    }

    /**
     * Gets a mock server for book numbers api
     *
     * @throws URISyntaxException
     * @throws JsonProcessingException
     */
    private void createMockServerStandard() throws URISyntaxException, JsonProcessingException {
        IsbnNumbers isbnNumbers = new IsbnNumbers();
        isbnNumbers.setIsbn10("0123456789");
        isbnNumbers.setIsbn13("0123456789012");
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(API_BOOKS)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(isbnNumbers))
                );
    }

    /**
     * Creates a mockserver which hangs
     *
     * @throws URISyntaxException
     * @throws JsonProcessingException
     */
    private void createMockServerTimeout() throws URISyntaxException, JsonProcessingException {
        IsbnNumbers isbnNumbers = new IsbnNumbers();
        isbnNumbers.setIsbn10("0123456789");
        isbnNumbers.setIsbn13("0123456789012");
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(API_BOOKS)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(request -> {
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return new MockClientHttpResponse(isbnNumbers.toString().getBytes(), HttpStatus.OK);
                });
    }

    @Test
    void should_get_a_random_book() {
        var book = testRestTemplate.getForEntity("http://127.0.0.1:" + port + "/api/books/random", Book.class).getBody();
        assertNotNull(book.getId());
    }


    @Test
    void should_find_all_books() throws Exception {
        var requestEntity = RequestEntity.get(new URI("http://127.0.0.1:" + port + "/api/books")).accept(MediaType.APPLICATION_JSON).build();
        var books = testRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<Book>>() {
        }).getBody();
        assertNotNull(books);
        assertEquals(1, books.size());
    }

    @Test
    void should_get_a_count() throws Exception {
        var requestEntity = RequestEntity.get(new URI("http://127.0.0.1:" + port + "/api/books/count")).accept(MediaType.APPLICATION_JSON).build();
        var books = testRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<Map<String, Long>>() {
        }).getBody();
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals(1, books.get("books.count"));
    }

    @Test
    void should_find_a_book() throws Exception {
        var responseEntity = testRestTemplate.getForEntity("http://127.0.0.1:" + port + "/api/books/100", Book.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        var book = responseEntity.getBody();
        assertNotNull(book);
        assertEquals(100L, book.getId());
    }

    @Test
    void should_find_no_book() throws Exception {
        var responseEntity = testRestTemplate.getForEntity("http://127.0.0.1:" + port + "/api/books/999", Book.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertFalse(responseEntity.hasBody());
    }

    @Test
    void should_register_a_book_successfully() throws Exception {
        createMockServerStandard();
        Book book = new Book();
        book.setAuthor("George Orwell");
        book.setTitle("Animal's farm");
        var responseEntity = testRestTemplate.postForEntity("http://127.0.0.1:" + port + "/api/books", book, Book.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        var uri = responseEntity.getHeaders().getLocation();
        assertNotNull(uri);
        assertTrue(uri.getPath().matches(".*/api/books/[1-9]+$"));
        mockServer.verify();
    }

    @Test
    void should_throw_a_timeout_while_registering() throws Exception {
        createMockServerTimeout();
        Book book = new Book();
        book.setAuthor("George Orwell");
        book.setTitle("Animal's farm");
        var responseEntity = testRestTemplate.postForEntity("http://127.0.0.1:" + port + "/api/books", book, Book.class);
        assertEquals(HttpStatus.REQUEST_TIMEOUT, responseEntity.getStatusCode());
    }

    @Test
    void should_update_book() throws Exception {
        Book book = new Book();
        book.setId(100L);
        book.setAuthor("George Orwell");
        book.setTitle("Animal's farm");
        var responseEntity = testRestTemplate.exchange("http://127.0.0.1:" + port + "/api/books", HttpMethod.PUT, new HttpEntity<>(book), Book.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void should_delete_book() throws Exception {
        testRestTemplate.delete("http://127.0.0.1:" + port + "/api/books/100");
        var responseEntity = testRestTemplate.getForEntity("http://127.0.0.1:" + port + "/api/books/100", Book.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
