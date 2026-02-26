package com.evalflow.eval_flow.controller;

import com.evalflow.eval_flow.model.Batch;
import com.evalflow.eval_flow.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/batches")
public class BatchController {


    @Autowired
    BatchRepository batchRepository;

    @PostMapping
    public Batch createBatch(@RequestBody Batch batch) {
        return batchRepository.save(batch);
    }

    @GetMapping
    public List<Batch> getAllBatches(){
        return  batchRepository.findAll();
    }
}
