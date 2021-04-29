package org.techforum.spring.number.controller;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.techforum.spring.number.dto.BookNumbers;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/books", produces = APPLICATION_JSON_VALUE)
public class BookNumbersController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BookNumbersController.class);

    @Value("${umber.separator:false}")
    private boolean separator;

    @Value("${time.to.sleep:15}")
    private int timeToSleep;

    @PostConstruct
    public void postConstruct() {
        LOGGER.info("JJS => timeToSleep=" + timeToSleep);
    }


    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BookNumbers> generateBookNumbers() throws InterruptedException {

        LOGGER.info("Generating book numbers, sleeping " + timeToSleep + " msec");

        if (timeToSleep != 0)
            TimeUnit.MILLISECONDS.sleep(timeToSleep);

        Faker faker = new Faker();
        BookNumbers bookNumbers = new BookNumbers();
        bookNumbers.setIsbn10(faker.code().isbn10(separator));
        bookNumbers.setIsbn13(faker.code().isbn13(separator));
        bookNumbers.setAsin(faker.code().asin());
        bookNumbers.setEan8(faker.code().ean8());
        bookNumbers.setEan13(faker.code().ean13());
        bookNumbers.setGenerationDate(Instant.now());

        return ResponseEntity.ok(bookNumbers);
    }

}
