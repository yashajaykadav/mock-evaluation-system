package com.evalflow.eval_flow.controller;


import com.evalflow.eval_flow.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/api/participants")
    @ResponseBody
    public List<ReportService.ParticipantReport> getParticipantReport(
            @RequestParam Long batchId,
            @RequestParam Long technologyId) {
        return reportService.getParticipantScoresReport(batchId, technologyId);
    }

    @GetMapping("/api/rounds")
    @ResponseBody
    public List<ReportService.RoundReport> getRoundReport(@RequestParam Long technologyId) {
        return reportService.getRoundAverageReport(technologyId);
    }

    @GetMapping("/api/export/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @RequestParam Long batchId,
            @RequestParam Long technologyId) {
        try {
            byte[] pdfBytes = reportService.generatePdfReport(batchId, technologyId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "evaluation-report.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/export/csv")
    public ResponseEntity<String> exportCsv(
            @RequestParam Long batchId,
            @RequestParam Long technologyId) {
        try {
            String csv = reportService.generateCsvReport(batchId, technologyId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "evaluation-report.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csv);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}