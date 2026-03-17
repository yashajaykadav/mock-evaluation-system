package com.evalflow.eval_flow.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EvaluationResponse {

    private Long id;
    private int score;
    private String feedback;
    private String result;
    private LocalDateTime submittedAt;

    private Long participantId;
    private Long evaluatorId;
}