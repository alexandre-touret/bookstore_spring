package info.touret.bookstore.spring.number;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;


@SpringBootApplication
public class RestNumbersApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestNumbersApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(RestNumbersApplication.class, args);
    }
}
