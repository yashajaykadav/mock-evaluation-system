package com.evalflow.eval_flow.repository;

import com.evalflow.eval_flow.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    Optional<Batch> findByName(String name);
    List<Batch> findByActiveTrue();
}