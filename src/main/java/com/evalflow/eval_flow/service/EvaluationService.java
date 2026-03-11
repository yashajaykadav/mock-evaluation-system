//package com.evalflow.eval_flow.service;
//
//import com.evalflow.eval_flow.model.*;
//import com.evalflow.eval_flow.repository.EvaluationRepository;
//import com.evalflow.eval_flow.repository.EvaluationRoundRepository;
//import com.evalflow.eval_flow.repository.ParticipantRepository;
//import com.evalflow.eval_flow.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//public class EvaluationService {
//
//    @Autowired
//    private EvaluationRepository evaluationRepository;
//
//    @Autowired
//    private EvaluationRoundRepository roundRepository;
//
//    @Autowired
//    private ParticipantRepository participantRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // Round Configuration
//    public EvaluationRound configureRounds(Batch batch, Technology technology, Integer totalRounds) {
//        EvaluationRound round = new EvaluationRound();
//        round.setBatch(batch);
//        round.setTechnology(technology);
//        round.setTotalRounds(totalRounds);
//        return roundRepository.save(round);
//    }
//
//    public EvaluationRound createRound(EvaluationRound round) {
//        return roundRepository.save(round);
//    }
//
//    public List<EvaluationRound> getRoundsByBatchAndTechnology(Long batchId, Long technologyId) {
//        Batch batch = new Batch();
//        batch.setId(batchId);
//        Technology technology = new Technology();
//        technology.setId(technologyId);
//        return roundRepository.findByBatchAndTechnology(batch, technology);
//    }
//
//    // Participant Management
//    public Participant createParticipant(Participant participant) {
//        return participantRepository.save(participant);
//    }
//
//    public List<Participant> getParticipantsByBatchAndTechnology(Long batchId, Long technologyId) {
//        Batch batch = new Batch();
//        batch.setId(batchId);
//        Technology technology = new Technology();
//        technology.setId(technologyId);
//        return participantRepository.findByBatchAndTechnology(batch, technology);
//    }
//
//    public List<Participant> getAllParticipants() {
//        return participantRepository.findAll();
//    }
//
//    public Optional<Participant> getParticipantById(Long id) {
//        return participantRepository.findById(id);
//    }
//
//    public Participant updateParticipant(Long id, Participant participantDetails) {
//        Participant participant = participantRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Participant not found"));
//
//        participant.setName(participantDetails.getName());
//        participant.setEmail(participantDetails.getEmail());
//        participant.setBatch(participantDetails.getBatch());
//        participant.setTechnology(participantDetails.getTechnology());
//        participant.setActive(participantDetails.isActive());
//
//        return participantRepository.save(participant);
//    }
//
//    // Evaluation Assignment
//    public Evaluation assignEvaluation(Long participantId, Long evaluatorId, Long roundId) {
//        Participant participant = participantRepository.findById(participantId)
//                .orElseThrow(() -> new RuntimeException("Participant not found"));
//        User evaluator = userRepository.findById(evaluatorId)
//                .orElseThrow(() -> new RuntimeException("Evaluator not found"));
//        EvaluationRound round = roundRepository.findById(roundId)
//                .orElseThrow(() -> new RuntimeException("Round not found"));
//
//        Evaluation evaluation = new Evaluation();
//        evaluation.setParticipant(participant);
//        evaluation.setEvaluator(evaluator);
//        evaluation.setRound(round);
//        evaluation.setStatus(Evaluation.Status.PENDING);
//
//        return evaluationRepository.save(evaluation);
//    }
//
//    // Evaluation Submission
//    public Evaluation submitEvaluation(Long evaluationId, Double score, String comments) {
//        Evaluation evaluation = evaluationRepository.findById(evaluationId)
//                .orElseThrow(() -> new RuntimeException("Evaluation not found"));
//
//        evaluation.setScore(score);
//        evaluation.setComments(comments);
//        evaluation.setStatus(Evaluation.Status.COMPLETED);
//        evaluation.setEvaluatedAt(LocalDateTime.now());
//
//        return evaluationRepository.save(evaluation);
//    }
//
//    // Retrieval Methods
//    public List<Evaluation> getEvaluationsByEvaluator(Long evaluatorId) {
//        User evaluator = new User();
//        evaluator.setId(evaluatorId);
//        return evaluationRepository.findByEvaluator(evaluator);
//    }
//
//    public List<Evaluation> getEvaluationsByParticipant(Long participantId) {
//        Participant participant = new Participant();
//        participant.setId(participantId);
//        return evaluationRepository.findByParticipant(participant);
//    }
//
//    public Optional<Evaluation> getEvaluationById(Long id) {
//        return evaluationRepository.findById(id);
//    }
//
//    public List<Evaluation> getAllEvaluations() {
//        return evaluationRepository.findAll();
//    }
//}