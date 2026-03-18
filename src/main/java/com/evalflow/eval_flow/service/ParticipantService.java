package com.evalflow.eval_flow.service;

import com.evalflow.eval_flow.entity.Participant;
import com.evalflow.eval_flow.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public Participant createParticipant(Participant participant){
        return participantRepository.save(participant);
    }

    public List<Participant> getAllParticipants(){
        return participantRepository.findAll();
    }

    public Participant updateParticipant(Long id ,Participant participant){

        Participant existing = participantRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Participant Not Found"));

        existing.setName(participant.getName());
        existing.setEmail(participant.getEmail());
        existing.setBatch(participant.getBatch());
        existing.setTechnology(participant.getTechnology());
        return participantRepository.save(existing);

    }

    public void deleteParticipant(Long id){
        participantRepository.deleteById(id);
    }
}
