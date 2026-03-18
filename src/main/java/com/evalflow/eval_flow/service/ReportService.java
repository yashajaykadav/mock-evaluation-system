package com.evalflow.eval_flow.service;

import com.evalflow.eval_flow.entity.*;
import com.evalflow.eval_flow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final EvaluationRepository evaluationRepository;
    private final EvaluationAssignmentRepository assignmentRepository;

    // -------------------------
    // DTOs
    // -------------------------

    public static class ParticipantReport {
        public Long participantId;
        public String participantName;
        public String participantEmail;
        public Double averageScore;
        public int totalRounds;
        public int completedRounds;
        public String status;
        public List<RoundScore> roundScores;
    }

    public static class RoundScore {
        public Long roundId;
        public String roundName;
        public int roundOrder;
        public Double score; // ✅ Changed from Integer to Double
        public String result;
    }

    public static class RoundReport {
        public Long roundId;
        public String roundName;
        public int roundOrder;
        public Double averageScore;
        public int completedEvaluations;
        public int totalEvaluations;
    }

    // -------------------------
    // Participant Score Report
    // -------------------------

    public List<ParticipantReport> getParticipantScoresReport(Long batchId, Long technologyId) {
        // Get all assignments for this batch+technology
        List<EvaluationAssignment> assignments = assignmentRepository.findAll().stream()
                .filter(a -> {
                    Participant p = a.getParticipant();
                    return p != null
                            && p.getBatch() != null
                            && p.getBatch().getId().equals(batchId)
                            && p.getTechnology() != null
                            && p.getTechnology().getId().equals(technologyId);
                })
                .collect(Collectors.toList());

        // Group by participant
        Map<Participant, List<EvaluationAssignment>> byParticipant = assignments.stream()
                .collect(Collectors.groupingBy(EvaluationAssignment::getParticipant));

        List<ParticipantReport> reports = new ArrayList<>();

        for (Map.Entry<Participant, List<EvaluationAssignment>> entry : byParticipant.entrySet()) {
            Participant participant = entry.getKey();
            List<EvaluationAssignment> pAssignments = entry.getValue();

            ParticipantReport report = new ParticipantReport();
            report.participantId = participant.getId();
            report.participantName = participant.getName();
            report.participantEmail = participant.getEmail();
            report.totalRounds = pAssignments.size();
            report.roundScores = new ArrayList<>();

            double totalScore = 0;
            int completedCount = 0;

            for (EvaluationAssignment assignment : pAssignments) {
                Optional<Evaluation> evalOpt = evaluationRepository.findByAssignment(assignment);

                RoundScore rs = new RoundScore();
                rs.roundId = assignment.getRound() != null ? assignment.getRound().getId() : null;
                rs.roundName = assignment.getRound() != null ? assignment.getRound().getRoundName() : "Round";
                rs.roundOrder = assignment.getRound() != null ? assignment.getRound().getRoundOrder() : 0;

                if (evalOpt.isPresent()) {
                    Evaluation eval = evalOpt.get();
                    rs.score = eval.getScore(); // ✅ Now returns double
                    rs.result = eval.getResult();
                    totalScore += eval.getScore();
                    completedCount++;
                } else {
                    rs.score = null;
                    rs.result = "PENDING";
                }
                report.roundScores.add(rs);
            }

            report.completedRounds = completedCount;
            report.averageScore = completedCount > 0 ? totalScore / completedCount : 0.0;
            report.status = (completedCount == pAssignments.size() && !pAssignments.isEmpty()) ? "Completed"
                    : "In Progress";

            reports.add(report);
        }

        reports.sort(Comparator.comparing(r -> r.participantName));
        return reports;
    }

    // -------------------------
    // Round Average Report
    // -------------------------

    public List<RoundReport> getRoundAverageReport(Long technologyId) {
        List<EvaluationAssignment> assignments = assignmentRepository.findAll().stream()
                .filter(a -> a.getRound() != null
                        && a.getRound().getTechnology() != null
                        && a.getRound().getTechnology().getId().equals(technologyId))
                .collect(Collectors.toList());

        Map<Round, List<EvaluationAssignment>> byRound = assignments.stream()
                .collect(Collectors.groupingBy(EvaluationAssignment::getRound));

        List<RoundReport> reports = new ArrayList<>();

        for (Map.Entry<Round, List<EvaluationAssignment>> entry : byRound.entrySet()) {
            Round round = entry.getKey();
            List<EvaluationAssignment> rAssignments = entry.getValue();

            RoundReport report = new RoundReport();
            report.roundId = round.getId();
            report.roundName = round.getRoundName();
            report.roundOrder = round.getRoundOrder();
            report.totalEvaluations = rAssignments.size();

            List<Evaluation> completed = new ArrayList<>();
            for (EvaluationAssignment a : rAssignments) {
                evaluationRepository.findByAssignment(a).ifPresent(completed::add);
            }

            report.completedEvaluations = completed.size();
            // ✅ Changed from mapToInt to mapToDouble
            report.averageScore = completed.stream()
                    .mapToDouble(Evaluation::getScore)
                    .average()
                    .orElse(0.0);

            reports.add(report);
        }

        reports.sort(Comparator.comparingInt(r -> r.roundOrder));
        return reports;
    }

    // -------------------------
    // CSV Export (pure Java)
    // -------------------------

    public String generateCsvReport(Long batchId, Long technologyId) {
        List<ParticipantReport> reports = getParticipantScoresReport(batchId, technologyId);

        StringBuilder sb = new StringBuilder();
        sb.append("Participant Name,Email,Completed Rounds,Total Rounds,Average Score,Status\n");

        for (ParticipantReport r : reports) {
            sb.append(escapeCsv(r.participantName)).append(",");
            sb.append(escapeCsv(r.participantEmail)).append(",");
            sb.append(r.completedRounds).append(",");
            sb.append(r.totalRounds).append(",");
            sb.append(String.format("%.2f", r.averageScore)).append(",");
            sb.append(escapeCsv(r.status)).append("\n");
        }

        return sb.toString();
    }

    private String escapeCsv(String value) {
        if (value == null)
            return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}