package com.evalflow.eval_flow.service;

import com.evalflow.eval_flow.model.Batch;
import com.evalflow.eval_flow.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;

    public Batch createBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    public Optional<Batch> getBatchById(Long id) {
        return batchRepository.findById(id);
    }

    public Optional<Batch> getBatchByName(String name) {
        return batchRepository.findByName(name);
    }

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public List<Batch> getActiveBatches() {
        return batchRepository.findByActiveTrue();
    }

    public Batch updateBatch(Long id, Batch batchDetails) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        batch.setName(batchDetails.getName());
        batch.setStartDate(batchDetails.getStartDate());
        batch.setEndDate(batchDetails.getEndDate());
        batch.setActive(batchDetails.isActive());

        return batchRepository.save(batch);
    }

    public void deleteBatch(Long id) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
        batch.setActive(false);
        batchRepository.save(batch);
    }
}