package com.evalflow.eval_flow.service;

import com.evalflow.eval_flow.entity.EvaluationAssignment;
import com.evalflow.eval_flow.repository.EvaluationAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationAssignmentService {

    private final EvaluationAssignmentRepository assignmentRepository;

    public EvaluationAssignment createAssignment(EvaluationAssignment assignment) {
        assignment.setStatus("ASSIGNED");
        return assignmentRepository.save(assignment);
    }

    public List<EvaluationAssignment> getAssignments() {
        return assignmentRepository.findAll();
    }

    public List<EvaluationAssignment> getAssignmentsForEvaluator(Long evaluatorId) {
        return assignmentRepository.findByEvaluatorId(evaluatorId);
    }

    public List<EvaluationAssignment> getPendingAssignments(Long evaluatorId){
        return assignmentRepository.findByEvaluator_IdAndStatus(evaluatorId,"ASSIGNED");
    }

}