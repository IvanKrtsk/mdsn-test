package org.ikrotsyuk.mdsn.booktrackerservice.repository;

import org.ikrotsyuk.mdsn.booktrackerservice.entity.AvailableBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailableBooksRepository extends JpaRepository<AvailableBookEntity, Integer> {
    @Query("SELECT a FROM AvailableBookEntity a WHERE a.isAvailable = true")
    List<AvailableBookEntity> findAllByIsAvailableTrue();
}
