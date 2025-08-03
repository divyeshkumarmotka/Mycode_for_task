package com.motka.workforcemgmt.repository;

import com.motka.workforcemgmt.common.model.TaskActivity;
import com.motka.workforcemgmt.common.model.TaskComment;

import java.util.List;

public interface TaskHistoryRepository {

    void addComment(TaskComment comment);

    void addActivity(TaskActivity activity);

    List<TaskComment> getCommentsByTaskId(Long taskId);

    List<TaskActivity> getActivitiesByTaskId(Long taskId);
}
