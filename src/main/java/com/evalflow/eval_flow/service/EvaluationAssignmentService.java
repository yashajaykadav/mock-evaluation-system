package com.evalflow.eval_flow.service;

import com.evalflow.eval_flow.entity.EvaluationAssignment;
import com.evalflow.eval_flow.entity.Participant;
import com.evalflow.eval_flow.entity.Round;
import com.evalflow.eval_flow.entity.User;
import com.evalflow.eval_flow.repository.EvaluationAssignmentRepository;
import com.evalflow.eval_flow.repository.ParticipantRepository;
import com.evalflow.eval_flow.repository.RoundRepository;
import com.evalflow.eval_flow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationAssignmentService {

    private final EvaluationAssignmentRepository assignmentRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final RoundRepository roundRepository;

    public EvaluationAssignment createAssignment(EvaluationAssignment assignment) {
        assignment.setStatus("ASSIGNED");
        return assignmentRepository.save(assignment);
    }

    public EvaluationAssignment createAssignmentByIds(Long participantId, Long evaluatorId, Long roundId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found: " + participantId));
        User evaluator = userRepository.findById(evaluatorId)
                .orElseThrow(() -> new RuntimeException("Evaluator not found: " + evaluatorId));
        Round round = roundRepository.findById(roundId)
                .orElseThrow(() -> new RuntimeException("Round not found: " + roundId));

        EvaluationAssignment assignment = EvaluationAssignment.builder()
                .participant(participant)
                .evaluator(evaluator)
                .round(round)
                .status("ASSIGNED")
                .build();

        return assignmentRepository.save(assignment);
    }

    public List<EvaluationAssignment> getAssignments() {
        return assignmentRepository.findAll();
    }

    public List<EvaluationAssignment> getAssignmentsForEvaluator(Long evaluatorId) {
        return assignmentRepository.findByEvaluatorId(evaluatorId);
    }

    public List<EvaluationAssignment> getPendingAssignments(Long evaluatorId) {
        return assignmentRepository.findByEvaluator_IdAndStatus(evaluatorId, "ASSIGNED");
    }

}