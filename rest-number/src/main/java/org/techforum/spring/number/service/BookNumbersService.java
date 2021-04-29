package org.techforum.spring.number.service;

import com.github.javafaker.Faker;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.techforum.spring.number.dto.BookNumbers;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class BookNumbersService {

    private final static Logger LOGGER = LoggerFactory.getLogger(BookNumbersService.class);


    @Value("${number.separator:false}")
    private boolean separator;

    @Value("${time.to.sleep:15}")
    private int timeToSleep;

    @PostConstruct
    public void postConstruct() {
        LOGGER.info("JJS => timeToSleep=" + timeToSleep);
    }

    @Bulkhead(name = "book-numbers")
    @TimeLimiter(name = "book-numbers")
    public CompletableFuture<BookNumbers> createBookNumbers() {
        LOGGER.info("Generating book numbers, sleeping " + timeToSleep + " msec");

        try {
            if (timeToSleep != 0)
                TimeUnit.MILLISECONDS.sleep(timeToSleep);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }

        Faker faker = new Faker();
        BookNumbers bookNumbers = new BookNumbers();
        bookNumbers.setIsbn10(faker.code().isbn10(separator));
        bookNumbers.setIsbn13(faker.code().isbn13(separator));
        bookNumbers.setAsin(faker.code().asin());
        bookNumbers.setEan8(faker.code().ean8());
        bookNumbers.setEan13(faker.code().ean13());
        bookNumbers.setGenerationDate(Instant.now());
        return CompletableFuture.completedFuture(bookNumbers);
    }
}
