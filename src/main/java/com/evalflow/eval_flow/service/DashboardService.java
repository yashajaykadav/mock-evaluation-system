package com.evalflow.eval_flow.service;

import com.evalflow.eval_flow.entity.Role;
import com.evalflow.eval_flow.repository.BatchRepository;
import com.evalflow.eval_flow.repository.EvaluationRepository;
import com.evalflow.eval_flow.repository.ParticipantRepository;
import com.evalflow.eval_flow.repository.UserRepository;
import com.evalflow.eval_flow.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final BatchRepository batchRepository;
    private final ParticipantRepository participantRepository;
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final TechnologyRepository technologyRepository;

    public Map<String, Object> getSummaryStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalBatches", batchRepository.count());
        stats.put("totalParticipants", participantRepository.count());
        stats.put("totalTechnologies", technologyRepository.count());
        stats.put("totalEvaluators", userRepository.findByRole(Role.EVALUATOR).size());

        long completedEvaluations = evaluationRepository.findAll().size();
        stats.put("completedEvaluations", completedEvaluations);

        return stats;
    }
}
