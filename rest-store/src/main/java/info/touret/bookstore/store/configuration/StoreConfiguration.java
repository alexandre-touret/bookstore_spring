package info.touret.bookstore.store.configuration;

import com.eventstore.dbclient.Client;
import com.eventstore.dbclient.ClientSettings;
import com.eventstore.dbclient.ConnectionSettingsBuilder;
import com.eventstore.dbclient.Endpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreConfiguration {

    @Bean(destroyMethod = "shutdown")
    public Client createClient() {
        ClientSettings clientSettings = new ConnectionSettingsBuilder().addHost(new Endpoint("127.0.0.1", 2113)).tls(false).buildConnectionSettings();
        return Client.create(clientSettings);
    }
}
