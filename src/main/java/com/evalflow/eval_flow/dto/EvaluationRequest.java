package com.evalflow.eval_flow.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EvaluationRequest {

    @Min(0)
    @Max(10)
    private int score;

    @NotBlank
    private String feedback;
}