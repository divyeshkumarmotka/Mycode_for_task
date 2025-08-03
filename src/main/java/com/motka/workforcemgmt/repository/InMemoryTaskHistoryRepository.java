package com.motka.workforcemgmt.repository;

import com.motka.workforcemgmt.common.model.TaskActivity;
import com.motka.workforcemgmt.common.model.TaskComment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryTaskHistoryRepository implements TaskHistoryRepository {

    private final List<TaskComment> comments = new ArrayList<>();
    private final List<TaskActivity> activities = new ArrayList<>();
    private final AtomicLong commentIdGen = new AtomicLong(1);
    private final AtomicLong activityIdGen = new AtomicLong(1);

    @Override
    public void addComment(TaskComment comment) {
        comment.setId(commentIdGen.getAndIncrement());
        comments.add(comment);
    }

    @Override
    public void addActivity(TaskActivity activity) {
        activity.setId(activityIdGen.getAndIncrement());
        activities.add(activity);
    }

    @Override
    public List<TaskComment> getCommentsByTaskId(Long taskId) {
        return comments.stream()
                .filter(c -> c.getTaskId().equals(taskId))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskActivity> getActivitiesByTaskId(Long taskId) {
        return activities.stream()
                .filter(a -> a.getTaskId().equals(taskId))
                .collect(Collectors.toList());
    }
}
