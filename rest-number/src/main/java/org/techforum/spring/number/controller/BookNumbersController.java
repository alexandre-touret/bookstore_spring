package org.techforum.spring.number.controller;

import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.techforum.spring.number.dto.BookNumbers;
import org.techforum.spring.number.service.BookNumbersService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Timed
@RestController
@RequestMapping(value = "/api/books", produces = APPLICATION_JSON_VALUE)
public class BookNumbersController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BookNumbersController.class);

    private BookNumbersService bookNumbersService;

    public BookNumbersController(BookNumbersService bookNumbersService) {
        this.bookNumbersService = bookNumbersService;
    }

    @TimeLimiter(name = "book-numbers", fallbackMethod = "generateBookNumbersFallBack")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Gets book numbers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book numbers",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookNumbers.class))}),
            @ApiResponse(responseCode = "504", description = "Timeout error",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookNumbers.class))})})
    public CompletableFuture<BookNumbers> generateBookNumbers() {
        return CompletableFuture.supplyAsync(() -> bookNumbersService.createBookNumbers());
    }

    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler({TimeoutException.class})
    public CompletableFuture<BookNumbers> generateBookNumbersFallBack(TimeoutException e) {
        LOGGER.error(e.getMessage(), e);
        return CompletableFuture.failedFuture(e);
    }
}
