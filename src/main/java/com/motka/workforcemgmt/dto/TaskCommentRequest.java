package com.motka.workforcemgmt.dto;

import lombok.Data;

@Data
public class TaskCommentRequest {
    private Long taskId;
    private Long userId;
    private String comment;
}
