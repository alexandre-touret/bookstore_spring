package info.touret.bookstore.store.dto;

import java.io.Serializable;

public class BookDTO implements Serializable {

    public  Long bookId;

    public BookDTO(Long bookId) {
        this.bookId = bookId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
