package com.evalflow.eval_flow.repository;

import com.evalflow.eval_flow.model.Batch;
import com.evalflow.eval_flow.model.EvaluationRound;
import com.evalflow.eval_flow.model.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRoundRepository extends JpaRepository<EvaluationRound, Long> {
    List<EvaluationRound> findByBatchAndTechnology(Batch batch, Technology technology);
    Optional<EvaluationRound> findByBatchAndTechnologyAndRoundNumber(
            Batch batch, Technology technology, Integer roundNumber);
}