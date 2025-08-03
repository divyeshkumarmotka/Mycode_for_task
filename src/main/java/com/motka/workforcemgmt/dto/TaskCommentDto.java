package com.motka.workforcemgmt.dto;

import lombok.Data;

@Data
public class TaskCommentDto {
    private Long taskId;
    private Long userId;
    private String comment;
    private Long timestamp; // Optional, can be generated in service
}
