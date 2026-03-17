package com.evalflow.eval_flow.repository;

import com.evalflow.eval_flow.entity.Evaluation;
import com.evalflow.eval_flow.entity.EvaluationAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    Optional<Evaluation> findByAssignmentId(Long assignmentId);
    boolean existsByAssignment(EvaluationAssignment assignment);

        List<Evaluation> findByAssignment_Participant_Id(Long participantId);

        List<Evaluation> findByAssignment_Evaluator_Id(Long evaluatorId);
    }