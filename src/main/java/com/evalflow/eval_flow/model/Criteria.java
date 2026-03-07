package com.evalflow.eval_flow.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Criteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int weightage;

    @ManyToOne
    @JoinColumn(name = "tech_id")
    private Technology technology;

}
