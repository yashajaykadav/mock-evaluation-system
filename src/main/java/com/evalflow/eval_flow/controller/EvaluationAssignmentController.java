package com.evalflow.eval_flow.controller;

import com.evalflow.eval_flow.entity.EvaluationAssignment;
import com.evalflow.eval_flow.service.EvaluationAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
@RequiredArgsConstructor
public class EvaluationAssignmentController {

    private final EvaluationAssignmentService assignmentService;

    @PostMapping
    public EvaluationAssignment createAssignment(@RequestBody EvaluationAssignment assignment) {
        return assignmentService.createAssignment(assignment);
    }

    @GetMapping
    public List<EvaluationAssignment> getAssignments() {
        return assignmentService.getAssignments();
    }

    @GetMapping("/evaluator/{id}")
    public List<EvaluationAssignment> getEvaluatorAssignments(@PathVariable Long id) {
        return assignmentService.getAssignmentsForEvaluator(id);
    }

    @GetMapping("/pending/{evaluatorId}")
    public ResponseEntity<List<EvaluationAssignment>> getPendingAssignments(@PathVariable Long evaluatorId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsForEvaluator(evaluatorId));
    }


}