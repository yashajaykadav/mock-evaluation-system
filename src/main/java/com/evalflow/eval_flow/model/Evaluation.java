package com.evalflow.eval_flow.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Participant participant;

    @ManyToOne
    private Technology technology;

    private int roundNumber;
    private int score;

    @Column(length = 500)
    private String feedback;

    private String evaluatorName;
}
