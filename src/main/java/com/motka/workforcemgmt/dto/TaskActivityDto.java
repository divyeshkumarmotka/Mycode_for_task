package com.motka.workforcemgmt.dto;

import lombok.Data;

@Data
public class TaskActivityDto {
    private Long taskId;
    private String message;
    private Long timestamp;
}