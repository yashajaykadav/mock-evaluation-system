package com.evalflow.eval_flow.repository;

import com.evalflow.eval_flow.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant,Long> {
    List<Participant> findByBatchId (Long batchId);
}
