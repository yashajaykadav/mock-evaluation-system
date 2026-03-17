package com.evalflow.eval_flow.repository;

import com.evalflow.eval_flow.entity.EvaluationAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationAssignmentRepository extends JpaRepository<EvaluationAssignment, Long> {

    List<EvaluationAssignment> findByEvaluatorId(Long evaluatorId);
    List<EvaluationAssignment> findByEvaluator_IdAndStatus(Long evaluatorId, String status);

}