package com.evalflow.eval_flow.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

}
