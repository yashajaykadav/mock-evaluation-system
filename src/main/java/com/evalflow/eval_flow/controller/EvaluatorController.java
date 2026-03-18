package com.evalflow.eval_flow.controller;

import com.evalflow.eval_flow.dto.EvaluationRequest;
import com.evalflow.eval_flow.dto.EvaluationResponse;
import com.evalflow.eval_flow.entity.EvaluationAssignment;
import com.evalflow.eval_flow.service.EvaluationAssignmentService;
import com.evalflow.eval_flow.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/evaluator/api")
@RequiredArgsConstructor
public class EvaluatorController {

    private final EvaluationAssignmentService assignmentService;
    private final EvaluationService evaluationService;

    /**
     * Get ALL assignments — used by evaluator dashboard to show all evaluations.
     */
    @GetMapping("/evaluations")
    public ResponseEntity<List<EvaluationAssignment>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAssignments());
    }

    /**
     * Get all pending/assigned evaluations for a specific evaluator.
     */
    @GetMapping("/assignments/{evaluatorId}")
    public ResponseEntity<List<EvaluationAssignment>> getMyAssignments(@PathVariable Long evaluatorId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsForEvaluator(evaluatorId));
    }

    /**
     * Get pending (ASSIGNED) assignments for evaluator.
     */
    @GetMapping("/assignments/{evaluatorId}/pending")
    public ResponseEntity<List<EvaluationAssignment>> getPendingAssignments(@PathVariable Long evaluatorId) {
        return ResponseEntity.ok(assignmentService.getPendingAssignments(evaluatorId));
    }

    /**
     * Submit an evaluation (score + feedback) — matches JS path: POST /evaluator/api/evaluations/{id}/submit
     */
    @PostMapping("/evaluations/{assignmentId}/submit")
    public ResponseEntity<EvaluationResponse> submitEvaluation(
            @PathVariable Long assignmentId,
            @RequestBody EvaluationRequest request) {
        try {
            EvaluationResponse response = evaluationService.submitEvaluation(assignmentId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .header("X-Error-Message", e.getMessage())
                    .build();
        }
    }

    /**
     * Alternative path: POST /evaluator/api/evaluations/submit/{assignmentId}
     */
    @PostMapping("/evaluations/submit/{assignmentId}")
    public ResponseEntity<EvaluationResponse> submitEvaluationAlt(
            @PathVariable Long assignmentId,
            @RequestBody EvaluationRequest request) {
        return submitEvaluation(assignmentId, request);
    }

    /**
     * Get all evaluations the evaluator has already completed.
     */
    @GetMapping("/evaluations/{evaluatorId}")
    public ResponseEntity<Map<String, Object>> getCompletedEvaluations(@PathVariable Long evaluatorId) {
        var evaluations = evaluationService.getByEvaluator(evaluatorId);
        return ResponseEntity.ok(Map.of("evaluations", evaluations, "count", evaluations.size()));
    }
}