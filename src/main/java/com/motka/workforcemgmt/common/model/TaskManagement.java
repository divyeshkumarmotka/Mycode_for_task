package com.motka.workforcemgmt.common.model;

import com.motka.workforcemgmt.common.model.enums.ReferenceType;
import com.motka.workforcemgmt.common.model.enums.Priority;
import com.motka.workforcemgmt.common.model.enums.Task;
import com.motka.workforcemgmt.common.model.enums.TaskStatus;
import com.motka.workforcemgmt.common.model.enums.TaskStatus;
import lombok.Data;

import java.util.List;

@Data
public class TaskManagement {
    private Long id;
    private Long referenceId;
    private ReferenceType referenceType;
    private Task task;
    private String description;
    private TaskStatus status;
    private Long assigneeId; // Simplified from Entity for this assignment
    private Long taskDeadlineTime;
    private Priority priority;

    // add for bug fixing
    private Long createdDate;

    // 3 feture
    private List<String> comments;
    private List<String> activities;

}
