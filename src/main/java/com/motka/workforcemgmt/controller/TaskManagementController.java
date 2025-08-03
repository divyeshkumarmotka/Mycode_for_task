package com.motka.workforcemgmt.controller;

import com.motka.workforcemgmt.common.model.response.Response;
import com.motka.workforcemgmt.dto.*;
import com.motka.workforcemgmt.service.TaskManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-mgmt")
public class TaskManagementController {

    private final TaskManagementService taskManagementService;
    private final TaskManagementService taskHistoryService; // ✅ Add this line

    // ✅ Constructor injection for both services
    public TaskManagementController(TaskManagementService taskManagementService,
                                    TaskManagementService taskHistoryService) {
        this.taskManagementService = taskManagementService;
        this.taskHistoryService = taskHistoryService;
    }

    @GetMapping("/{id}")
    public Response<TaskManagementDto> getTaskById(@PathVariable Long id) {
        return new Response<>(taskManagementService.findTaskById(id));
    }

    @PostMapping("/create")
    public Response<List<TaskManagementDto>> createTasks(@RequestBody TaskCreateRequest request) {
        System.out.println("Received: " + request);
        return new Response<>(taskManagementService.createTasks(request));
    }

    @PostMapping("/update")
    public Response<List<TaskManagementDto>> updateTasks(@RequestBody UpdateTaskRequest request) {
        return new Response<>(taskManagementService.updateTasks(request));
    }

    @PostMapping("/assign-by-ref")
    public Response<String> assignByReference(@RequestBody AssignByReferenceRequest request) {
        return new Response<>(taskManagementService.assignByReference(request));
    }

    @PostMapping("/fetch-by-date/v2")
    public Response<List<TaskManagementDto>> fetchByDate(@RequestBody TaskFetchByDateRequest request) {
        return new Response<>(taskManagementService.fetchTasksByDate(request));
    }

    // Feature 2: Update task priority
    @PostMapping("/task/update-priority")
    public ResponseEntity<TaskManagementDto> updatePriority(@RequestBody UpdateTaskPriorityRequest request) {
        TaskManagementDto updated = taskManagementService.updateTaskPriority(request);
        return ResponseEntity.ok(updated);
    }

    // Feature 3
    @PostMapping("/task/comment")
    public ResponseEntity<String> addComment(@RequestBody TaskCommentRequest request) {
        taskHistoryService.addComment(request.getTaskId(), request.getUserId(), request.getComment());
        taskHistoryService.logActivity(request.getTaskId(), "User " + request.getUserId() + " commented: " + request.getComment());
        return ResponseEntity.ok("Comment added successfully");
    }

}
