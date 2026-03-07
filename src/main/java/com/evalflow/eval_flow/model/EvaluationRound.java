package com.evalflow.eval_flow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "evaluation_rounds")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "technology_id")
    private Technology technology;

    @NotNull
    private Integer roundNumber;

    private String roundName;

    @Column(name = "total_rounds")
    private Integer totalRounds;
}