package info.touret.bookstore.store.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class StoreEvent implements Serializable {
    private Instant occuredTime;
    private Instant recordedTime;
    private String bookId;
    private EventType eventType;
    private UUID eventId;

    public StoreEvent(UUID eventId, Instant occuredTime, Instant recordedTime, String bookId, EventType eventType) {
        this.occuredTime = occuredTime;
        this.recordedTime = recordedTime;
        this.bookId = bookId;
        this.eventType = eventType;
        this.eventId = eventId;
    }

    public StoreEvent() {
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public Instant getOccuredTime() {
        return occuredTime;
    }

    public void setOccuredTime(Instant occuredTime) {
        this.occuredTime = occuredTime;
    }

    public Instant getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(Instant recordedTime) {
        this.recordedTime = recordedTime;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
