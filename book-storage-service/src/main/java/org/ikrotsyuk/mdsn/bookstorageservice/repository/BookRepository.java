package org.ikrotsyuk.mdsn.bookstorageservice.repository;

import org.ikrotsyuk.mdsn.bookstorageservice.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    BookEntity findFirstByIsbn(String isbn);
    Boolean existsByIsbn(String isbn);
    List<BookEntity> findAllByIsDeletedFalse();
}
