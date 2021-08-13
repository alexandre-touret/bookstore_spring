package info.touret.bookstore.store.service;

import com.eventstore.dbclient.Client;
import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventDataBuilder;
import com.eventstore.dbclient.ReadResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import info.touret.bookstore.store.EventStoreException;
import info.touret.bookstore.store.model.StoreEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class StoreService {
    //esdb://127.0.0.1:2113?tls=false
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreService.class);

    private Client eventStoreClient;

    public StoreService(Client eventStoreClient) {
        this.eventStoreClient = eventStoreClient;
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public void store(StoreEvent storeEvent) throws JsonProcessingException {
        ObjectMapper objectMapper = createObjectMapper();
        EventData eventData = EventDataBuilder.json(storeEvent.getEventType().toString(), objectMapper.writeValueAsString(storeEvent)).build();
        var writeResultCompletableFuture = eventStoreClient.streams().appendStream(storeEvent.getBookId()).addEvent(eventData).execute();
        writeResultCompletableFuture.whenComplete((writeResult1, throwable) -> {
            if (throwable != null) {
                LOGGER.error(throwable.getMessage(), throwable);
            } else {
                LOGGER.info("New Revision : [{}]", writeResult1.getNextExpectedRevision().getValueUnsigned());
            }
        });
    }


    public List<StoreEvent> readForwards(String bookId) {
        var readResultCompletableFuture = eventStoreClient.streams().readStream(bookId).forward().fromStart().readThrough();

        try {
            ReadResult readResult = readResultCompletableFuture.get();
            return readResult.getEvents()
                    .stream()
                    .map(resolvedEvent -> {
                        byte[] data = resolvedEvent.getEvent().getEventData();
                        try {
                            ObjectMapper objectMapper = createObjectMapper();
                            return objectMapper.readValue(data, new StoreEvent().getClass());
                        } catch (IOException e) {
                            throw new EventStoreException(e);
                        }
                    }).collect(Collectors.toUnmodifiableList());
        } catch (InterruptedException | ExecutionException e) {
            throw new EventStoreException(e);
        }
    }
}
