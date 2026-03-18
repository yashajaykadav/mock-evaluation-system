package com.evalflow.eval_flow.controller;

import com.evalflow.eval_flow.entity.Round;
import com.evalflow.eval_flow.service.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rounds")
@RequiredArgsConstructor
public class RoundController {

    private final RoundService roundService;

    @PostMapping
    public Round createRound(@RequestBody Round round) {
        return roundService.createRound(round);
    }

    @GetMapping
    public List<Round> getRounds() {
        return roundService.getRounds();
    }
}