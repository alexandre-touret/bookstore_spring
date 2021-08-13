package info.touret.bookstore.store.dto;

import info.touret.bookstore.store.model.EventType;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class BookStoreResult implements Serializable {
    private String eventId;
    private EventType eventType;
    private ZonedDateTime lastUpdatedTime;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public ZonedDateTime getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(ZonedDateTime lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
