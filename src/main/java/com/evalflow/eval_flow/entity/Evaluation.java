package com.evalflow.eval_flow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "assignment_id")
    private EvaluationAssignment assignment;

    private double score;

    @Column(length = 2000)
    private String feedback;

    private String result;

    private LocalDateTime submittedAt;

}