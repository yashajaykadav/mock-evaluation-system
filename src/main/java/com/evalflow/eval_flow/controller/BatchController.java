// package com.evalflow.eval_flow.controller;

// import com.evalflow.eval_flow.entity.Batch;
// import com.evalflow.eval_flow.service.BatchService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/batches")
// @RequiredArgsConstructor
// public class BatchController {

//     private final BatchService batchService;

//     @PostMapping
//     public Batch createBatch(@RequestBody Batch batch) {
//         return batchService.createBatch(batch);
//     }

//     @GetMapping
//     public List<Batch> getAllBatches() {
//         return batchService.getAllBatches();
//     }

//     @PutMapping("/{id}")
//     public Batch updateBatch(@PathVariable Long id, @RequestBody Batch batch) {
//         return batchService.updateBatch(id, batch);
//     }

//     @DeleteMapping("/{id}")
//     public void deleteBatch(@PathVariable Long id) {
//         batchService.deleteBatch(id);
//     }
// }