package org.techforum.spring.number.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.techforum.spring.number.dto.BookNumbers;
import org.techforum.spring.number.service.BookNumbersService;

import java.util.concurrent.ExecutionException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/books", produces = APPLICATION_JSON_VALUE)
public class BookNumbersController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BookNumbersController.class);

    private BookNumbersService bookNumbersService;

    public BookNumbersController(BookNumbersService bookNumbersService) {
        this.bookNumbersService = bookNumbersService;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BookNumbers> generateBookNumbers() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(bookNumbersService.createBookNumbers().get());
    }

}
