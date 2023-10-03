package info.touret.bookstore.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring boot class
 */
@SpringBootApplication
public class RestBookstoreApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestBookstoreApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(RestBookstoreApplication.class, args);
    }

}
