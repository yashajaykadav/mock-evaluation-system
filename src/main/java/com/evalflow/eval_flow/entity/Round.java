package com.evalflow.eval_flow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rounds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roundName;

    private int roundOrder;

    @ManyToOne
    @JoinColumn(name = "technology_id")
    private Technology technology;

}