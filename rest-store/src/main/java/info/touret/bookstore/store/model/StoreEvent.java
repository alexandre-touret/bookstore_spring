package info.touret.bookstore.store.model;

import info.touret.bookstore.store.dto.BookDTO;

import java.time.Instant;

public class StoreEvent {
    private Instant occuredTime;
    private Instant recordedTime;
    private String bookId;
    private EventType eventType;

    public StoreEvent(Instant occuredTime, Instant recordedTime, String bookId, EventType eventType) {
        this.occuredTime = occuredTime;
        this.recordedTime = recordedTime;
        this.bookId = bookId;
        this.eventType = eventType;
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
