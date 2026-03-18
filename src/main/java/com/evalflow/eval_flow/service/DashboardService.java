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

        // ✅ Keys that match JavaScript expectations
        stats.put("batches", batchRepository.count());
        stats.put("activeBatches", batchRepository.count()); // Add active filter if needed
        stats.put("participants", participantRepository.count());
        stats.put("technologies", technologyRepository.count());
        stats.put("evaluators", userRepository.findByRole(Role.EVALUATOR).size());
        stats.put("completedEvaluations", evaluationRepository.findAll().size());

        return stats;
    }
}