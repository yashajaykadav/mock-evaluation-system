//package com.evalflow.eval_flow.controller;
//
//
//import com.evalflow.eval_flow.model.EvaluationAssignment;
//import com.evalflow.eval_flow.repository.AssignmentRepository;
//import org.springframework.beans.factory.annotation.*;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/admin/assigments")
//public class AssignmentController {
//
//    @Autowired
//    private AssignmentRepository assignmentRepository;
//
//    @PostMapping
//    public EvaluationAssignment createAssignment(@RequestBody EvaluationAssignment assignment) {
//        return assignmentRepository.save(assignment);
//    }
//
//    @GetMapping
//    public List<EvaluationAssignment> getAll() {
//        return assignmentRepository.findAll();
//    }
//}
