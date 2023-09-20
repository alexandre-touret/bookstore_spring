package info.touret.bookstore.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.Executors;

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
