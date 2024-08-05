package com.api.repository;

import com.api.entities.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    @Override
    List<Train> findAll();
    Optional<Train> findById(Long id);
    @Query(value = "select * from trains order by id desc limit :limit", nativeQuery = true)
    List<Train> getAll(@Param("limit") int limit);
}
