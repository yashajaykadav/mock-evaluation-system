package com.evalflow.eval_flow.controller;

import com.evalflow.eval_flow.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/participants")
    public ResponseEntity<List<ReportService.ParticipantReport>> getParticipantReport(
            @RequestParam Long batchId,
            @RequestParam Long technologyId) {
        return ResponseEntity.ok(reportService.getParticipantScoresReport(batchId, technologyId));
    }

    @GetMapping("/rounds")
    public ResponseEntity<List<ReportService.RoundReport>> getRoundReport(@RequestParam Long technologyId) {
        return ResponseEntity.ok(reportService.getRoundAverageReport(technologyId));
    }

    @GetMapping("/export/csv")
    public ResponseEntity<String> exportCsv(
            @RequestParam Long batchId,
            @RequestParam Long technologyId) {
        String csv = reportService.generateCsvReport(batchId, technologyId);
        return ResponseEntity.ok()
                .header("Content-Type", "text/csv")
                .header("Content-Disposition", "attachment; filename=\"evaluation-report.csv\"")
                .body(csv);
    }
}