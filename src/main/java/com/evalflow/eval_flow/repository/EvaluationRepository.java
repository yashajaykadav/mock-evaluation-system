package com.evalflow.eval_flow.repository;

import com.evalflow.eval_flow.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByEvaluator(User evaluator);
    List<Evaluation> findByParticipant(Participant participant);
    List<Evaluation> findByRound(EvaluationRound round);
    List<Evaluation> findByStatus(Evaluation.Status status);

    Optional<Evaluation> findByParticipantAndRound(Participant participant, EvaluationRound round);

    @Query("SELECT e FROM Evaluation e WHERE e.participant.batch = :batch AND e.participant.technology = :technology")
    List<Evaluation> findByBatchAndTechnology(@Param("batch") Batch batch, @Param("technology") Technology technology);

    @Query("SELECT AVG(e.score) FROM Evaluation e WHERE e.round = :round AND e.status = 'COMPLETED'")
    Double getAverageScoreByRound(@Param("round") EvaluationRound round);
}