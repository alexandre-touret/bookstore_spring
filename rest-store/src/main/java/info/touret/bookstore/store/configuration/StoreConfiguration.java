package info.touret.bookstore.store.configuration;

import com.eventstore.dbclient.ConnectionSettingsBuilder;
import com.eventstore.dbclient.Endpoint;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreConfiguration {

    @Bean(destroyMethod = "shutdown")
    public EventStoreDBClient createClient() {
        EventStoreDBClientSettings clientSettings = new ConnectionSettingsBuilder().addHost(new Endpoint("127.0.0.1", 2113)).tls(false).buildConnectionSettings();
        return EventStoreDBClient.create(clientSettings);
    }
}
