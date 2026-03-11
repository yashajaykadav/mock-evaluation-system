package com.evalflow.eval_flow.service;

import com.evalflow.eval_flow.entity.Batch;
import com.evalflow.eval_flow.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final BatchRepository batchRepository;

    public Batch createBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public Batch updateBatch(Long id, Batch batch) {
        Batch existing = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        existing.setName(batch.getName());
        existing.setStartDate(batch.getStartDate());
        existing.setEndDate(batch.getEndDate());

        return batchRepository.save(existing);
    }

    public void deleteBatch(Long id) {
        batchRepository.deleteById(id);
    }
}