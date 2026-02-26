package com.evalflow.eval_flow.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;
}
