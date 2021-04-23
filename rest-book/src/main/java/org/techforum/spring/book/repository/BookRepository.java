package org.techforum.spring.book.repository;

import org.techforum.spring.book.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
