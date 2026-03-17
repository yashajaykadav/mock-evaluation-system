package com.evalflow.eval_flow.controller;


import com.evalflow.eval_flow.dto.EvaluationRequest;
import com.evalflow.eval_flow.dto.EvaluationResponse;
import com.evalflow.eval_flow.entity.Evaluation;
import com.evalflow.eval_flow.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping("/submit/{assignmentId}")
    public ResponseEntity<EvaluationResponse> submit(
            @PathVariable Long assignmentId,
            @Valid @RequestBody EvaluationRequest request) {

        return ResponseEntity.ok(
                evaluationService.submitEvaluation(assignmentId, request)
        );
    }

    @GetMapping("/participant/{id}")
    public ResponseEntity<List<Evaluation>>getByParticipant(@PathVariable Long id){
        return ResponseEntity.ok(evaluationService.getByParticipant(id));
    }

    @GetMapping("/evaluator/{id}")
    public ResponseEntity<List<Evaluation>> getByEvaluator(@PathVariable Long id){
        return ResponseEntity.ok(evaluationService.getByEvaluator(id));
    }

    @GetMapping
    public ResponseEntity<List<Evaluation>> getAll(){
        return ResponseEntity.ok(evaluationService.getAll());
    }
}
