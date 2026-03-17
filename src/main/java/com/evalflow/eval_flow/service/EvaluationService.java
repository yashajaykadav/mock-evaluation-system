package com.evalflow.eval_flow.service;


import com.evalflow.eval_flow.dto.EvaluationRequest;
import com.evalflow.eval_flow.dto.EvaluationResponse;
import com.evalflow.eval_flow.entity.Evaluation;
import com.evalflow.eval_flow.entity.EvaluationAssignment;
import com.evalflow.eval_flow.repository.EvaluationAssignmentRepository;
import com.evalflow.eval_flow.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final EvaluationAssignmentRepository assignmentRepository;

    public EvaluationResponse submitEvaluation(Long assignmentId, EvaluationRequest request) {

        EvaluationAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getStatus().equals("ASSIGNED")) {
            throw new RuntimeException("Assignment is not valid");
        }

        if (evaluationRepository.existsByAssignment(assignment)) {
            throw new RuntimeException("Evaluation already exists");
        }

        Evaluation evaluation = Evaluation.builder()
                .assignment(assignment)
                .score(request.getScore())
                .feedback(request.getFeedback())
                .result(request.getScore() >= 7 ? "SELECTED" : "REJECTED")
                .submittedAt(LocalDateTime.now())
                .build();

        assignment.setStatus("COMPLETED");
        assignmentRepository.save(assignment);

        Evaluation saved = evaluationRepository.save(evaluation);

        return mapToResponse(saved);
    }

    private EvaluationResponse mapToResponse(Evaluation eval) {
        return EvaluationResponse.builder()
                .id(eval.getId())
                .score(eval.getScore())
                .feedback(eval.getFeedback())
                .result(eval.getResult())
                .submittedAt(eval.getSubmittedAt())
                .participantId(eval.getAssignment().getParticipant().getId())
                .evaluatorId(eval.getAssignment().getEvaluator().getId())
                .build();
    }

    public List<Evaluation> getByParticipant(Long participantId) {
        return evaluationRepository.findByAssignment_Participant_Id(participantId);
    }

    public List<Evaluation> getByEvaluator(Long evaluatorId) {
        return evaluationRepository.findByAssignment_Evaluator_Id(evaluatorId);
    }

    public List<Evaluation> getAll() {
        return evaluationRepository.findAll();
    }

}
