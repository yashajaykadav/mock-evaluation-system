package com.evalflow.eval_flow.service;

import com.evalflow.eval_flow.model.Technology;
import com.evalflow.eval_flow.repository.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TechnologyService {

    @Autowired
    private TechnologyRepository technologyRepository;

    public Technology createTechnology(Technology technology) {
        return technologyRepository.save(technology);
    }

    public Optional<Technology> getTechnologyById(Long id) {
        return technologyRepository.findById(id);
    }

    public Optional<Technology> getTechnologyByName(String name) {
        return technologyRepository.findByName(name);
    }

    public List<Technology> getAllTechnologies() {
        return technologyRepository.findAll();
    }

    public List<Technology> getActiveTechnologies() {
        return technologyRepository.findByActiveTrue();
    }

    public Technology updateTechnology(Long id, Technology technologyDetails) {
        Technology technology = technologyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technology not found"));

        technology.setName(technologyDetails.getName());
        technology.setDescription(technologyDetails.getDescription());
        technology.setActive(technologyDetails.isActive());

        return technologyRepository.save(technology);
    }

    public void deleteTechnology(Long id) {
        Technology technology = technologyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technology not found"));
        technology.setActive(false);
        technologyRepository.save(technology);
    }
}