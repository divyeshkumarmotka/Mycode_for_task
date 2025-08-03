package com.motka.workforcemgmt.common.model;

import lombok.Data;

@Data
public class TaskActivity {
    private Long id;
    private Long taskId;
    private String activity;  // e.g., "User A updated status to COMPLETED"
    private Long timestamp;
}
