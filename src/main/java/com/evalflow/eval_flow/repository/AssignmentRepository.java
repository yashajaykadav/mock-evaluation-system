//package com.evalflow.eval_flow.repository;
//
//import com.evalflow.eval_flow.model.EvaluationAssignment;
//import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.List;
//
//public interface AssignmentRepository extends JpaRepository<EvaluationAssignment, Long> {
//    // Used by Evaluators to see their "To-Do" list
//    List<EvaluationAssignment> findByAssignedEvaluatorEmailAndIsCompletedFalse(String email);
//}