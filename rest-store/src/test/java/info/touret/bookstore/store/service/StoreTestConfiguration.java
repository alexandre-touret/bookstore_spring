package info.touret.bookstore.store.service;

import info.touret.bookstore.store.configuration.StoreConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("info.touret.bookstore.store")
@Import(StoreConfiguration.class)
public class StoreTestConfiguration {

}
