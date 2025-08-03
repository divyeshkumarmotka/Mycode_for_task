package com.motka.workforcemgmt.service.impl;

import com.motka.workforcemgmt.common.exception.ResourceNotFoundException;
import com.motka.workforcemgmt.dto.*;
import com.motka.workforcemgmt.mapper.ITaskManagementMapper;
import com.motka.workforcemgmt.common.model.TaskManagement;
import com.motka.workforcemgmt.common.model.enums.Task;
import com.motka.workforcemgmt.common.model.enums.TaskStatus;
import com.motka.workforcemgmt.repository.TaskRepository;
import com.motka.workforcemgmt.service.TaskManagementService;
import org.springframework.stereotype.Service;
import com.motka.workforcemgmt.dto.TaskCommentDto;
import com.motka.workforcemgmt.dto.TaskActivityDto;


import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskRepository taskRepository;
    private final ITaskManagementMapper taskMapper;

    public TaskManagementServiceImpl(TaskRepository taskRepository, ITaskManagementMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskManagementDto findTaskById(Long id) {
        TaskManagement task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        TaskManagementDto dto = taskMapper.modelToDto(task);

        // Comments & activities ko add karo
        List<String> commentList = comments.getOrDefault(id, new ArrayList<>());
        List<String> activityList = activities.getOrDefault(id, new ArrayList<>());

        dto.setComments(commentList);
        dto.setActivities(activityList);

        return dto;
    }


    @Override
    public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
        List<TaskManagement> createdTasks = new ArrayList<>();

        for (TaskCreateRequest.RequestItem item : createRequest.getRequests()) {
            TaskManagement newTask = new TaskManagement();

            // ✅ Mapping all fields from request to model
            newTask.setReferenceId(item.getReferenceId());
            newTask.setReferenceType(item.getReferenceType());
            newTask.setTask(item.getTask());
            newTask.setAssigneeId(item.getAssigneeId());
            newTask.setPriority(item.getPriority());
            newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
            newTask.setDescription(item.getDescription());
            newTask.setStatus(item.getStatus());
            newTask.setCreatedDate(item.getCreatedDate());
            newTask.setComments(item.getComments());
            newTask.setActivities(item.getActivities());

            createdTasks.add(taskRepository.save(newTask));
        }

        // Convert saved model list to response DTO list
        return taskMapper.modelListToDtoList(createdTasks);
    }


    @Override
    public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
        List<TaskManagement> updatedTasks = new ArrayList<>();
        for (UpdateTaskRequest.RequestItem item : updateRequest.getRequests()) {
            TaskManagement task = taskRepository.findById(item.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));

            if (item.getTaskStatus() != null) {
                task.setStatus(item.getTaskStatus());
            }
            if (item.getDescription() != null) {
                task.setDescription(item.getDescription());
            }
            updatedTasks.add(taskRepository.save(task));
        }
        return taskMapper.modelListToDtoList(updatedTasks);
    }

    @Override
    public String assignByReference(AssignByReferenceRequest request) {
        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
        List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(
                request.getReferenceId(), request.getReferenceType());

        for (Task taskType : applicableTasks) {
            List<TaskManagement> tasksOfType = existingTasks.stream()
                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
                    .collect(Collectors.toList());

            // solution of first #BUG - 1
            // Cancel all old tasks that are not already cancelled
            for (TaskManagement taskToCancel : tasksOfType) {
                if (taskToCancel.getStatus() != TaskStatus.CANCELLED) {
                    taskToCancel.setStatus(TaskStatus.CANCELLED);
                    taskRepository.save(taskToCancel);
                }
            }

            // Assign new task to the new assignee
            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(request.getReferenceId());
            newTask.setReferenceType(request.getReferenceType());
            newTask.setTask(taskType);
            newTask.setAssigneeId(request.getAssigneeId());
            newTask.setStatus(TaskStatus.ASSIGNED);
            taskRepository.save(newTask);
        }

        return "Tasks assigned successfully for reference " + request.getReferenceId();
    }


    @Override
    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
        List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());

        Long startDate = request.getStartDate();
        Long endDate = request.getEndDate();

        List<TaskManagement> filteredTasks = tasks.stream()
                .filter(task -> task.getStatus() != TaskStatus.CANCELLED) // cancelled task hatao
                .filter(task ->
                        // BUG - 2 Fixed
                        // CASE 1: All active tasks that started within that range
                                (task.getCreatedDate() != null &&
                                task.getCreatedDate() >= startDate &&
                                task.getCreatedDate() <= endDate)

                        // Implimentation of Feture 1
                        // CASE 2: all active tasks that started before the range but are still open and not yet completed
                                || (task.getCreatedDate() != null &&
                                task.getCreatedDate() < startDate &&
                                task.getStatus() != TaskStatus.COMPLETED)
                )
                .collect(Collectors.toList());

        return taskMapper.modelListToDtoList(filteredTasks);
    }

    @Override
    public TaskManagementDto updateTaskPriority(UpdateTaskPriorityRequest request) {
        TaskManagement task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + request.getTaskId()));

        task.setPriority(request.getNewPriority());
        taskRepository.save(task);

        return taskMapper.modelToDto(task);
    }

    // feture - 3

    private final Map<Long, List<String>> comments = new HashMap<>();
    private final Map<Long, List<String>> activities = new HashMap<>();

    @Override
    public void addComment(Long taskId, Long userId, String comment) {
        String fullComment = "User " + userId + " commented: " + comment;
        comments.computeIfAbsent(taskId, k -> new ArrayList<>()).add(fullComment);
    }

    @Override
    public void logActivity(Long taskId, String activity) {
        activities.computeIfAbsent(taskId, k -> new ArrayList<>()).add(activity);
    }



}



