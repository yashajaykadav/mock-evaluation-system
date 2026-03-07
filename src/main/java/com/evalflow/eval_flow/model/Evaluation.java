package com.evalflow.eval_flow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "evaluator_id")
    private User evaluator;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "round_id")
    private EvaluationRound round;

    private Double score;

    @Column(length = 2000)
    private String comments;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDateTime evaluatedAt;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Status {
        PENDING, COMPLETED
    }
}