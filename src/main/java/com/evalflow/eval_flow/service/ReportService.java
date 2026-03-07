package com.evalflow.eval_flow.service;

import com.evalflow.eval_flow.model.*;
import com.evalflow.eval_flow.repository.BatchRepository;
import com.evalflow.eval_flow.repository.EvaluationRepository;
import com.evalflow.eval_flow.repository.EvaluationRoundRepository;
import com.evalflow.eval_flow.repository.TechnologyRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private EvaluationRoundRepository roundRepository;

    // Report Data Models
    public static class ParticipantReport {
        public String participantName;
        public String participantEmail;
        public Map<Integer, Double> roundScores;
        public Double averageScore;
        public String status;
    }

    public static class RoundReport {
        public Integer roundNumber;
        public String roundName;
        public Double averageScore;
        public Integer completedEvaluations;
        public Integer totalEvaluations;
    }

    // Get Participant Scores Report
    public List<ParticipantReport> getParticipantScoresReport(Long batchId, Long technologyId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
        Technology technology = technologyRepository.findById(technologyId)
                .orElseThrow(() -> new RuntimeException("Technology not found"));

        List<Evaluation> evaluations = evaluationRepository.findByBatchAndTechnology(batch, technology);

        Map<Participant, List<Evaluation>> participantEvaluations = evaluations.stream()
                .collect(Collectors.groupingBy(Evaluation::getParticipant));

        List<ParticipantReport> reports = new ArrayList<>();

        for (Map.Entry<Participant, List<Evaluation>> entry : participantEvaluations.entrySet()) {
            ParticipantReport report = new ParticipantReport();
            report.participantName = entry.getKey().getName();
            report.participantEmail = entry.getKey().getEmail();
            report.roundScores = new HashMap<>();

            double totalScore = 0;
            int completedCount = 0;

            for (Evaluation eval : entry.getValue()) {
                if (eval.getStatus() == Evaluation.Status.COMPLETED && eval.getScore() != null) {
                    report.roundScores.put(eval.getRound().getRoundNumber(), eval.getScore());
                    totalScore += eval.getScore();
                    completedCount++;
                }
            }

            report.averageScore = completedCount > 0 ? totalScore / completedCount : 0.0;
            report.status = completedCount == entry.getValue().size() ? "Completed" : "In Progress";

            reports.add(report);
        }

        return reports;
    }

    // Get Round Average Report
    public List<RoundReport> getRoundAverageReport(Long technologyId) {
        Technology technology = technologyRepository.findById(technologyId)
                .orElseThrow(() -> new RuntimeException("Technology not found"));

        List<EvaluationRound> rounds = roundRepository.findAll().stream()
                .filter(r -> r.getTechnology().getId().equals(technologyId))
                .collect(Collectors.toList());

        List<RoundReport> reports = new ArrayList<>();

        for (EvaluationRound round : rounds) {
            RoundReport report = new RoundReport();
            report.roundNumber = round.getRoundNumber();
            report.roundName = round.getRoundName();

            List<Evaluation> evaluations = evaluationRepository.findByRound(round);
            long completedCount = evaluations.stream()
                    .filter(e -> e.getStatus() == Evaluation.Status.COMPLETED)
                    .count();

            report.completedEvaluations = (int) completedCount;
            report.totalEvaluations = evaluations.size();
            report.averageScore = evaluationRepository.getAverageScoreByRound(round);

            reports.add(report);
        }

        return reports;
    }

    // Generate PDF Report
    public byte[] generatePdfReport(Long batchId, Long technologyId) throws DocumentException, IOException {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
        Technology technology = technologyRepository.findById(technologyId)
                .orElseThrow(() -> new RuntimeException("Technology not found"));

        List<ParticipantReport> participantReports = getParticipantScoresReport(batchId, technologyId);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Evaluation Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));

        // Batch and Technology Info
        document.add(new Paragraph("Batch: " + batch.getName()));
        document.add(new Paragraph("Technology: " + technology.getName()));
        document.add(new Paragraph("Generated: " + new Date()));
        document.add(new Paragraph(" "));

        // Table
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 3, 2, 2});

        // Headers
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        PdfPCell cell = new PdfPCell(new Phrase("Participant Name", headerFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Email", headerFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Average Score", headerFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Status", headerFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        // Data
        for (ParticipantReport report : participantReports) {
            table.addCell(report.participantName);
            table.addCell(report.participantEmail);
            table.addCell(String.format("%.2f", report.averageScore));
            table.addCell(report.status);
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }

    // Generate CSV Report
    public String generateCsvReport(Long batchId, Long technologyId) throws IOException {
        List<ParticipantReport> participantReports = getParticipantScoresReport(batchId, technologyId);

        StringWriter sw = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT
                .withHeader("Participant Name", "Email", "Average Score", "Status"));

        for (ParticipantReport report : participantReports) {
            csvPrinter.printRecord(
                    report.participantName,
                    report.participantEmail,
                    String.format("%.2f", report.averageScore),
                    report.status
            );
        }

        csvPrinter.flush();
        return sw.toString();
    }
}