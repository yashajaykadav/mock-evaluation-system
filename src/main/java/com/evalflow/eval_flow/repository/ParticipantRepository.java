package com.evalflow.eval_flow.repository;

import com.evalflow.eval_flow.model.Batch;
import com.evalflow.eval_flow.model.Participant;
import com.evalflow.eval_flow.model.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByEmail(String email);
    List<Participant> findByBatch(Batch batch);
    List<Participant> findByTechnology(Technology technology);
    List<Participant> findByBatchAndTechnology(Batch batch, Technology technology);
    List<Participant> findByActiveTrue();
}