package com.evalflow.eval_flow.repository;


import com.evalflow.eval_flow.model.Evaluation;
import com.evalflow.eval_flow.model.EvaluationAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    // Use the underscore to resolve the nested property ambiguity
    List<Evaluation> findByParticipant_Batch_Id(Long batchId);
    List<EvaluationAssignment>findByAssignedEvaluatorEmailAndIsCompletedFalse(String email);
}