package com.motka.workforcemgmt.dto;

import com.motka.workforcemgmt.common.model.enums.Priority;
import lombok.Data;

@Data
public class UpdateTaskPriorityRequest {
    private Long taskId;
    private Priority newPriority;
}
