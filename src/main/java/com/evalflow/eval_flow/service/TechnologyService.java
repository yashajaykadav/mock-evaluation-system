package com.evalflow.eval_flow.service;

import com.evalflow.eval_flow.entity.Technology;
import com.evalflow.eval_flow.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    public Technology createTechnology(Technology technology) {
        return technologyRepository.save(technology);
    }

    public List<Technology> getAllTechnologies() {
        return technologyRepository.findAll();
    }

    public void deleteTechnology(Long id) {
        technologyRepository.deleteById(id);
    }
}