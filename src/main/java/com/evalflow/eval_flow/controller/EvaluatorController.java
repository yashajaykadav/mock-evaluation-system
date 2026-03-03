package com.evalflow.eval_flow.controller;


import com.evalflow.eval_flow.model.Evaluation;
import com.evalflow.eval_flow.repository.EvaluationRepository;
import com.evalflow.eval_flow.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluator")
public class EvaluatorController {
    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private EvaluationService evaluationService;

    @PostMapping("/submit")
    private String submitEvaluation(@RequestBody Evaluation evaluation){
        evaluationRepository.save(evaluation);
        return " Evaluation Submitted Successfully for participant Id: "+evaluation.getId();
    }

    @GetMapping("/report/average/{techId}/{round}")
    public double getAverage(@PathVariable Long techId, @PathVariable Integer round){
        return evaluationService.getAverageScore(techId, round);
    }
}
