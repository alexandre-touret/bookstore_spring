package org.techforum.spring.book.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.techforum.spring.book.entity.Book;

import java.util.List;

/**
 * Book Spring Data Repository
 */
public interface BookRepository extends CrudRepository<Book, Long> {

    @Query(value = "select b.id from Book b")
    List<Long> findAllIds();
}
