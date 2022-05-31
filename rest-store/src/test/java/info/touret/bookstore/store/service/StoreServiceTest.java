package info.touret.bookstore.store.service;

import com.eventstore.dbclient.EventStoreDBClient;
import info.touret.bookstore.store.model.EventType;
import info.touret.bookstore.store.model.StoreEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = StoreTestConfiguration.class)
class StoreServiceTest {

    @Autowired
    private EventStoreDBClient client;

    private StoreService storeService;

    @BeforeEach
    void setUp() {
        storeService = new StoreService(client);
    }

    @Test
    void store() throws Exception {
        storeService.store(new StoreEvent(UUID.randomUUID(), Instant.now(), Instant.now(), "1", EventType.BOUGHT));
    }

    @Test
    void readForward() {
        var storeEvents = storeService.readForwards("1");
        assertNotNull(storeEvents);
    }
}
