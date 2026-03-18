package com.evalflow.eval_flow.service;

import com.evalflow.eval_flow.entity.Round;
import com.evalflow.eval_flow.repository.RoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoundService {

    private final RoundRepository roundRepository;

    public Round createRound(Round round) {
        return roundRepository.save(round);
    }

    public List<Round> getRounds() {
        return roundRepository.findAll();
    }

    public List<Round> getRoundsByTechnology(Long technologyId) {
        return roundRepository.findByTechnologyId(technologyId);
    }

    public void deleteRound(Long id) {
        roundRepository.deleteById(id);
    }
}