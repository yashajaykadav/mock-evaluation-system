package com.evalflow.eval_flow.service;

import com.evalflow.eval_flow.model.Evaluation;
import com.evalflow.eval_flow.repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    /**
     * FR-4.2: Get average score for a specific technology and round.
     */
    public double getAverageScore(Long techId, int round) {
        List<Evaluation> evals = evaluationRepository.findAll();
        return evals.stream()
                .filter(e -> e.getTechnology().getId().equals(techId) && e.getRoundNumber() == round)
                .mapToInt(Evaluation::getScore)
                .average()
                .orElse(0.0);
    }

    /**
     * FR-4.2: Implementation for showing a summary of averages across all rounds.
     */
    public Map<Integer, Double> getAvgScorePerRound(Long techId) {
        List<Evaluation> evals = evaluationRepository.findAll();
        return evals.stream()
                .filter(e -> e.getTechnology().getId().equals(techId))
                .collect(Collectors.groupingBy(
                        Evaluation::getRoundNumber,
                        Collectors.averagingInt(Evaluation::getScore)
                ));
    }

    /**
     * FR-4.1: Fetch all results for a specific batch.
     */
    public List<Evaluation> getBatchEvaluations(Long batchId) {
        List<Evaluation> result = evaluationRepository.findByParticipant_Batch_Id(batchId);
        if(result.isEmpty()){
            throw  new RuntimeException("No Evaluation record found for this BatchId: "+batchId);
        }
        return result;
    }

    /**
     * FR-4.3: Implementation for Exporting reports to CSV format.
     */
    public String generateCsvContent(Long batchId) {
        List<Evaluation> evals = evaluationRepository.findByParticipant_Batch_Id(batchId);
        StringBuilder csv = new StringBuilder("Participant,Technology,Round,Score,Feedback\n");

        for (Evaluation e : evals) {
            // Fix: Ensure feedback is properly quoted and doesn't contain stray newlines
            String cleanFeedback = e.getFeedback().replace("\"", "'").replace("\n", " ");

            csv.append(e.getParticipant().getName()).append(",")
                    .append(e.getTechnology().getName()).append(",")
                    .append(e.getRoundNumber()).append(",")
                    .append(e.getScore()).append(",")
                    .append("\"").append(cleanFeedback).append("\"\n");
        }
        return csv.toString();
    }
}