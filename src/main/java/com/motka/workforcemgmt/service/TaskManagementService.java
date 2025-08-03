package com.motka.workforcemgmt.service;

import com.motka.workforcemgmt.dto.*;

import java.util.List;

public interface TaskManagementService {
    List<TaskManagementDto> createTasks(TaskCreateRequest request);
    List<TaskManagementDto> updateTasks(UpdateTaskRequest request);
    String assignByReference(AssignByReferenceRequest request);
    List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request);
    TaskManagementDto findTaskById(Long id);
    TaskManagementDto updateTaskPriority(UpdateTaskPriorityRequest request);

    void addComment(Long taskId, Long userId, String comment);
    void logActivity(Long taskId, String activity);

}
