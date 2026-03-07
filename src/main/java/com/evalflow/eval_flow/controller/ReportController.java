package com.evalflow.eval_flow.controller;


import com.evalflow.eval_flow.model.Evaluation;
import com.evalflow.eval_flow.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private EvaluationService evaluationService;

    @GetMapping("/average/{techId}")
    public Map<Integer,Double> getRoundAverage(@PathVariable Long techId){
        return evaluationService.getAvgScorePerRound(techId);
    }

    @GetMapping("/batch/{batchId}")
    public List<Evaluation> getBatchData(@PathVariable Long batchId){
        return evaluationService.getBatchEvaluations(batchId);
    }

    @GetMapping("/download-csv/{batchId}")
    public ResponseEntity<String> downloadCSV(@PathVariable Long batchId){
        String csvData = evaluationService.generateCsvContent(batchId);

        String filename= "Batch_"   + batchId +"_Report.csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+filename)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }

}
