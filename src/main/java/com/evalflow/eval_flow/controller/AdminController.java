//package com.evalflow.eval_flow.controller;
//
//
//import com.evalflow.eval_flow.model.*;
//import com.evalflow.eval_flow.service.*;
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.*;
//
//@Controller
//@RequestMapping("/admin")
//public class AdminController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private BatchService batchService;
//
//    @Autowired
//    private TechnologyService technologyService;
//
//    @Autowired
//    private EvaluationService evaluationService;
//
//    @GetMapping("/dashboard")
//    public String dashboard(HttpSession session, Model model) {
//        User user = (User) session.getAttribute("user");
//        if (user == null || user.getRole() != User.Role.ADMIN) {
//            return "redirect:/login";
//        }
//        model.addAttribute("user", user);
//        return "admin-dashboard";
//    }
//
//    // User Management APIs
//    @PostMapping("/api/users")
//    @ResponseBody
//    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
//        User createdUser = userService.createUser(user);
//        return ResponseEntity.ok(createdUser);
//    }
//
//    @GetMapping("/api/users")
//    @ResponseBody
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/api/users/evaluators")
//    @ResponseBody
//    public List<User> getEvaluators() {
//        return userService.getEvaluators();
//    }
//
//    @PutMapping("/api/users/{id}")
//    @ResponseBody
//    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
//        User updatedUser = userService.updateUser(id, user);
//        return ResponseEntity.ok(updatedUser);
//    }
//
//    @DeleteMapping("/api/users/{id}")
//    @ResponseBody
//    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.ok(Map.of("success", true));
//    }
//
//    // Batch Management APIs
//    @PostMapping("/api/batches")
//    @ResponseBody
//    public ResponseEntity<?> createBatch(@Valid @RequestBody Batch batch) {
//        Batch createdBatch = batchService.createBatch(batch);
//        return ResponseEntity.ok(createdBatch);
//    }
//
//    @GetMapping("/api/batches")
//    @ResponseBody
//    public List<Batch> getAllBatches() {
//        return batchService.getAllBatches();
//    }
//
//    @PutMapping("/api/batches/{id}")
//    @ResponseBody
//    public ResponseEntity<?> updateBatch(@PathVariable Long id, @Valid @RequestBody Batch batch) {
//        Batch updatedBatch = batchService.updateBatch(id, batch);
//        return ResponseEntity.ok(updatedBatch);
//    }
//
//    @DeleteMapping("/api/batches/{id}")
//    @ResponseBody
//    public ResponseEntity<?> deleteBatch(@PathVariable Long id) {
//        batchService.deleteBatch(id);
//        return ResponseEntity.ok(Map.of("success", true));
//    }
//
//    // Technology Management APIs
//    @PostMapping("/api/technologies")
//    @ResponseBody
//    public ResponseEntity<?> createTechnology(@Valid @RequestBody Technology technology) {
//        Technology createdTechnology = technologyService.createTechnology(technology);
//        return ResponseEntity.ok(createdTechnology);
//    }
//
//    @GetMapping("/api/technologies")
//    @ResponseBody
//    public List<Technology> getAllTechnologies() {
//        return technologyService.getAllTechnologies();
//    }
//
//    @PutMapping("/api/technologies/{id}")
//    @ResponseBody
//    public ResponseEntity<?> updateTechnology(@PathVariable Long id, @Valid @RequestBody Technology technology) {
//        Technology updatedTechnology = technologyService.updateTechnology(id, technology);
//        return ResponseEntity.ok(updatedTechnology);
//    }
//
//    @DeleteMapping("/api/technologies/{id}")
//    @ResponseBody
//    public ResponseEntity<?> deleteTechnology(@PathVariable Long id) {
//        technologyService.deleteTechnology(id);
//        return ResponseEntity.ok(Map.of("success", true));
//    }
//
//    // Round Configuration APIs
//    @PostMapping("/api/rounds")
//    @ResponseBody
//    public ResponseEntity<?> createRound(@Valid @RequestBody EvaluationRound round) {
//        EvaluationRound createdRound = evaluationService.createRound(round);
//        return ResponseEntity.ok(createdRound);
//    }
//
//    @GetMapping("/api/rounds")
//    @ResponseBody
//    public List<EvaluationRound> getRounds(@RequestParam Long batchId, @RequestParam Long technologyId) {
//        return evaluationService.getRoundsByBatchAndTechnology(batchId, technologyId);
//    }
//
//    // Participant Management APIs
//    @PostMapping("/api/participants")
//    @ResponseBody
//    public ResponseEntity<?> createParticipant(@Valid @RequestBody Participant participant) {
//        Participant createdParticipant = evaluationService.createParticipant(participant);
//        return ResponseEntity.ok(createdParticipant);
//    }
//
//    @GetMapping("/api/participants")
//    @ResponseBody
//    public List<Participant> getAllParticipants() {
//        return evaluationService.getAllParticipants();
//    }
//
//    @PutMapping("/api/participants/{id}")
//    @ResponseBody
//    public ResponseEntity<?> updateParticipant(@PathVariable Long id, @Valid @RequestBody Participant participant) {
//        Participant updatedParticipant = evaluationService.updateParticipant(id, participant);
//        return ResponseEntity.ok(updatedParticipant);
//    }
//
//    // Evaluation Assignment APIs
//    @PostMapping("/api/evaluations/assign")
//    @ResponseBody
//    public ResponseEntity<?> assignEvaluation(@RequestBody Map<String, Long> assignment) {
//        Evaluation evaluation = evaluationService.assignEvaluation(
//                assignment.get("participantId"),
//                assignment.get("evaluatorId"),
//                assignment.get("roundId")
//        );
//        return ResponseEntity.ok(evaluation);
//    }
//
//    @GetMapping("/api/evaluations")
//    @ResponseBody
//    public List<Evaluation> getAllEvaluations() {
//        return evaluationService.getAllEvaluations();
//    }
//}