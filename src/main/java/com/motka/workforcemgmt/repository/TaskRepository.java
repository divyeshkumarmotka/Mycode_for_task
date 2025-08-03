package com.motka.workforcemgmt.repository;

import com.motka.workforcemgmt.common.model.TaskManagement;
import com.motka.workforcemgmt.common.model.TaskManagement;
import com.motka.workforcemgmt.common.model.enums.Priority;
import com.motka.workforcemgmt.common.model.enums.TaskStatus;
import com.motka.workforcemgmt.common.model.enums.ReferenceType;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Optional<TaskManagement> findById(Long id);
    TaskManagement save(TaskManagement task);
    List<TaskManagement> findAll();
    List<TaskManagement> findByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType);
    List<TaskManagement> findByAssigneeIdIn(List<Long> assigneeIds);
    List<TaskManagement> findByPriority(Priority priority);
}
