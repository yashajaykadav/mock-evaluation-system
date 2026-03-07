//package com.evalflow.eval_flow.controller;
//
//
//import com.evalflow.eval_flow.model.Participant;
//import com.evalflow.eval_flow.repository.ParticipantRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/admin/participants")
//public class ParticipantController {
//
//    @Autowired
//    private ParticipantRepository  participantRepository;
//
//    @PostMapping
//    public Participant addParticipant(@RequestBody Participant participant){
//        return participantRepository.save(participant);
//    }
//
//    @GetMapping("/batch/{batchId}")
//    public List<Participant> getByBatch(@PathVariable Long batchId){
//        return participantRepository.findByBatchId(batchId);
//    }
//}
