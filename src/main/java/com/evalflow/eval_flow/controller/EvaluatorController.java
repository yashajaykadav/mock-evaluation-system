package com.evalflow.eval_flow.controller;

import com.evalflow.eval_flow.model.*;
import com.evalflow.eval_flow.service.EvaluationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/evaluator")
public class EvaluatorController {

    @Autowired
    private EvaluationService evaluationService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.EVALUATOR) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "evaluator-dashboard";
    }

    @GetMapping("/api/evaluations")
    @ResponseBody
    public List<Evaluation> getMyEvaluations(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return evaluationService.getEvaluationsByEvaluator(user.getId());
    }

    @GetMapping("/api/evaluations/{id}")
    @ResponseBody
    public ResponseEntity<?> getEvaluation(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Optional<Evaluation> evaluation = evaluationService.getEvaluationById(id);

        if (evaluation.isPresent() && evaluation.get().getEvaluator().getId().equals(user.getId())) {
            return ResponseEntity.ok(evaluation.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/api/evaluations/{id}/submit")
    @ResponseBody
    public ResponseEntity<?> submitEvaluation(
            @PathVariable Long id,
            @RequestBody Map<String, Object> data,
            HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            Optional<Evaluation> evaluationOpt = evaluationService.getEvaluationById(id);

            if (evaluationOpt.isEmpty() || !evaluationOpt.get().getEvaluator().getId().equals(user.getId())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Unauthorized"));
            }

            Double score = ((Number) data.get("score")).doubleValue();
            String comments = (String) data.get("comments");

            Evaluation evaluation = evaluationService.submitEvaluation(id, score, comments);
            return ResponseEntity.ok(evaluation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}