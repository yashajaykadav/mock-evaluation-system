package com.evalflow.eval_flow.repository;

import com.evalflow.eval_flow.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {

    List<Evaluation> findByParticipantBatchId(Iterable<Integer> integers);
}
