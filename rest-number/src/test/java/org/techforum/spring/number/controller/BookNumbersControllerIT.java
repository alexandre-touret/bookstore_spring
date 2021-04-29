package org.techforum.spring.number.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.techforum.spring.number.dto.BookNumbers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
class BookNumbersControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void should_get_book_numbers() {
        var response = restTemplate.getForEntity("http://127.0.0.1:" + port + "/api/books", BookNumbers.class);
        assertEquals(OK, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body.getAsin());
        assertNotNull(body.getEan8());
        assertNotNull(body.getEan13());
        assertNotNull(body.getIsbn10());
        assertNotNull(body.getIsbn13());
    }
}