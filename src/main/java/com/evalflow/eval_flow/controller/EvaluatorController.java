package com.evalflow.eval_flow.controller;


import com.evalflow.eval_flow.model.Evaluation;
import com.evalflow.eval_flow.model.EvaluationAssignment;
import com.evalflow.eval_flow.repository.AssignmentRepository;
import com.evalflow.eval_flow.repository.EvaluationRepository;
import com.evalflow.eval_flow.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluator")
public class EvaluatorController {
    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private AssignmentRepository  assignmentRepository;

    @Autowired
    private EvaluationService evaluationService;

    @PostMapping("/submit")
    private String submitEvaluation(@RequestBody Evaluation evaluation,@RequestParam Long assignmentId){
        evaluationRepository.save(evaluation);

        assignmentRepository.findById(assignmentId).ifPresent(assignment->{
            assignment.setCompleted(true);
            assignmentRepository.save(assignment);
        });
        return " Evaluation Submitted Successfully for participant Id: "+evaluation.getId();
    }

    @GetMapping("/report/average/{techId}/{round}")
    public double getAverage(@PathVariable Long techId, @PathVariable Integer round){
        return evaluationService.getAverageScore(techId, round);
    }

    @GetMapping("/assignments")
    public List<EvaluationAssignment> getMyAssignments(@RequestParam String email){
        return assignmentRepository.findByAssignedEvaluatorEmailAndIsCompletedFalse(email);
    }
}
