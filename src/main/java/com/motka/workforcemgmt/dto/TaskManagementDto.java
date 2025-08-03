package com.motka.workforcemgmt.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.motka.workforcemgmt.common.model.enums.ReferenceType;
import com.motka.workforcemgmt.common.model.enums.Priority;
import com.motka.workforcemgmt.common.model.enums.Task;
import com.motka.workforcemgmt.common.model.enums.TaskStatus;
import com.motka.workforcemgmt.dto.TaskActivityDto;
import com.motka.workforcemgmt.dto.TaskCommentDto;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskManagementDto {
    private Long id;
    private Long referenceId;
    private ReferenceType referenceType;
    private Task task;
    private String description;
    private TaskStatus status;
    private Long assigneeId;
    private Long taskDeadlineTime;
    private Priority priority;

    private Long createdDate;

    // Feature 3 additions (now with full DTOs instead of String)
    private List<String> comments;
    private List<String> activities;
}
