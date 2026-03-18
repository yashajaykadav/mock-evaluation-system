package com.evalflow.eval_flow.controller;

import com.evalflow.eval_flow.entity.Technology;
import com.evalflow.eval_flow.service.TechnologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/technologies")
@RequiredArgsConstructor
public class TechnologyController {

    private final TechnologyService technologyService;

    @PostMapping
    public Technology createTechnology(@RequestBody Technology technology) {
        return technologyService.createTechnology(technology);
    }

    @GetMapping
    public List<Technology> getTechnologies() {
        return technologyService.getAllTechnologies();
    }

    @DeleteMapping("/{id}")
    public void deleteTechnology(@PathVariable Long id) {
        technologyService.deleteTechnology(id);
    }
}