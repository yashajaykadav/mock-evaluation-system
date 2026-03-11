package com.evalflow.eval_flow.controller;


import com.evalflow.eval_flow.entity.Participant;
import com.evalflow.eval_flow.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @GetMapping
    public List<Participant> getParticipants(){
        return participantService.getAllParticipants();
    }

    @PostMapping
    public Participant createParticipant(@RequestBody Participant participant){
        return participantService.createParticipant(participant);
    }

    @PutMapping("{id}")
    public Participant updateParticipant(@PathVariable Long id , @RequestBody Participant participant){
        return participantService.updateParticipant(id, participant);
    }

    @DeleteMapping("{id}")
    public void deleteParticipant(@PathVariable Long id){
        participantService.deleteParticipant(id);
    }

}
