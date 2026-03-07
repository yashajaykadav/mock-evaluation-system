package com.evalflow.eval_flow.service;


import com.evalflow.eval_flow.model.Evaluation;
import com.evalflow.eval_flow.repository.BatchRepository;
import com.evalflow.eval_flow.repository.EvaluationRepository;
import com.evalflow.eval_flow.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private ParticipantRepository  participantRepository;

    @Autowired
    private EvaluationRepository  evaluationRepository;


    public Map<String,Object> getSummaryStats(){
        Map<String,Object> stats = new HashMap<>();

        stats.put("totalBatches",batchRepository.count());
        stats.put("totalParticipants",participantRepository.count());

        double avg = evaluationRepository.findAll()
                .stream()
                .mapToInt(Evaluation::getScore)
                .average()
                .orElse(0.0);

        stats.put("avg",avg);
        return stats;
    }

}
