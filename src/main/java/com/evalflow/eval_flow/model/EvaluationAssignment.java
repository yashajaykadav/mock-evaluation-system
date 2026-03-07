package com.evalflow.eval_flow.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EvaluationAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Participant participant;

    @ManyToOne
    private Technology  technology;

    private int roundNumber;
    private String assignedEvaluatorEmail;
    private boolean isCompleted = false;
}
