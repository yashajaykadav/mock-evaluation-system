package com.evalflow.eval_flow.controller;

import com.evalflow.eval_flow.entity.*;
import com.evalflow.eval_flow.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final BatchService batchService;
    private final TechnologyService technologyService;
    private final ParticipantService participantService;
    private final RoundService roundService;
    private final EvaluationAssignmentService assignmentService;
    private final DashboardService dashboardService;
    private final ReportService reportService;

    // ========== Dashboard ===========

    @GetMapping("/dashboard/summary")
    public Map<String, Object> getDashboardSummary() {
        return dashboardService.getSummaryStats();
    }

    // ========== Users ===========

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/evaluators")
    public List<User> getEvaluators() {
        return userService.getUsersByRole(Role.EVALUATOR);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ========== Batches ===========

    @GetMapping("/batches")
    public List<Batch> getAllBatches() {
        return batchService.getAllBatches();
    }

    @PostMapping("/batches")
    public ResponseEntity<Batch> createBatch(@RequestBody Batch batch) {
        return ResponseEntity.ok(batchService.createBatch(batch));
    }

    @PutMapping("/batches/{id}")
    public ResponseEntity<Batch> updateBatch(@PathVariable Long id, @RequestBody Batch batch) {
        return ResponseEntity.ok(batchService.updateBatch(id, batch));
    }

    @DeleteMapping("/batches/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteBatch(@PathVariable Long id) {
        batchService.deleteBatch(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ========== Technologies ===========

    @GetMapping("/technologies")
    public List<Technology> getAllTechnologies() {
        return technologyService.getAllTechnologies();
    }

    @PostMapping("/technologies")
    public ResponseEntity<Technology> createTechnology(@RequestBody Technology technology) {
        return ResponseEntity.ok(technologyService.createTechnology(technology));
    }

    @PutMapping("/technologies/{id}")
    public ResponseEntity<Technology> updateTechnology(@PathVariable Long id, @RequestBody Technology technology) {
        return ResponseEntity.ok(technologyService.updateTechnology(id, technology));
    }

    @DeleteMapping("/technologies/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteTechnology(@PathVariable Long id) {
        technologyService.deleteTechnology(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ========== Rounds ===========

    @GetMapping("/rounds")
    public List<Round> getRounds(@RequestParam(required = false) Long technologyId) {
        if (technologyId != null) {
            return roundService.getRoundsByTechnology(technologyId);
        }
        return roundService.getRounds();
    }

    @PostMapping("/rounds")
    public ResponseEntity<Round> createRound(@RequestBody Round round) {
        return ResponseEntity.ok(roundService.createRound(round));
    }

    @DeleteMapping("/rounds/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteRound(@PathVariable Long id) {
        roundService.deleteRound(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ========== Participants ===========

    @GetMapping("/participants")
    public List<Participant> getAllParticipants() {
        return participantService.getAllParticipants();
    }

    @PostMapping("/participants")
    public ResponseEntity<Participant> createParticipant(@RequestBody Participant participant) {
        return ResponseEntity.ok(participantService.createParticipant(participant));
    }

    @PutMapping("/participants/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable Long id, @RequestBody Participant participant) {
        return ResponseEntity.ok(participantService.updateParticipant(id, participant));
    }

    @DeleteMapping("/participants/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ========== Evaluation Assignments ===========

    @GetMapping("/evaluations")
    public List<EvaluationAssignment> getAllAssignments() {
        return assignmentService.getAssignments();
    }

    @PostMapping("/evaluations/assign")
    public ResponseEntity<EvaluationAssignment> assignEvaluation(@RequestBody Map<String, Long> body) {
        Long participantId = body.get("participantId");
        Long evaluatorId = body.get("evaluatorId");
        Long roundId = body.get("roundId");
        return ResponseEntity.ok(assignmentService.createAssignmentByIds(participantId, evaluatorId, roundId));
    }

    // ========== Reports ===========

    @GetMapping("/reports/participants")
    public ResponseEntity<List<ReportService.ParticipantReport>> getParticipantReport(
            @RequestParam Long batchId,
            @RequestParam Long technologyId) {
        return ResponseEntity.ok(reportService.getParticipantScoresReport(batchId, technologyId));
    }

    @GetMapping("/reports/rounds")
    public ResponseEntity<List<ReportService.RoundReport>> getRoundReport(@RequestParam Long technologyId) {
        return ResponseEntity.ok(reportService.getRoundAverageReport(technologyId));
    }

    @GetMapping("/reports/export/csv")
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