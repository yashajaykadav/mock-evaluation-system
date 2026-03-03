package com.evalflow.eval_flow.service;


import com.evalflow.eval_flow.model.Evaluation;
import com.evalflow.eval_flow.repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    public double getAverageScore(Long techId,int round){

        List<Evaluation> evals =evaluationRepository.findAll();

        return evals.stream()
                .filter(e->e.getTechnology().getId().equals(techId)&& e.getRoundNumber()==round)
                .mapToInt(Evaluation::getScore)
                .average()
                .orElse(0.0);
    }
}
