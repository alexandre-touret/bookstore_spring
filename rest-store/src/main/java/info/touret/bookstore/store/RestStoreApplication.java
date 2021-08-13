package info.touret.bookstore.store;

import info.touret.bookstore.store.configuration.StoreConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {"info.touret.bookstore.store.*"})
@Import(StoreConfiguration.class)
public class RestStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestStoreApplication.class, args);
    }
}
