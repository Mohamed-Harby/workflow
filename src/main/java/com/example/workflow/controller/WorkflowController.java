package com.example.workflow.controller;

import com.example.workflow.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.flowable.engine.TaskService;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workflow")
public class WorkflowController {
    private final WorkflowService workflowService;

    /**
     * Starts a process instance with the given process key and a default variable.
     *
     * @param processKey the key of the process definition
     */
    @GetMapping("/start/{processKey}")
    public void startProcess(@PathVariable String processKey) {
        workflowService.startProcess(processKey, Map.of());
    }

    /**
     * Handles starting a process instance from a form submission with generic variables.
     *
     * @param processKey the key of the process definition
     * @param variables map of variables to start the process with
     */
    @PostMapping("/start")
    public void handleStartForm(@RequestParam String processKey, @RequestBody Map<String, Object> variables) {
        workflowService.startProcess(processKey, variables);
    }

    /**
     * Suspends the process instance with the given ID.
     *
     * @param processInstanceId the ID of the process instance
     */
    @PostMapping("/process/{processInstanceId}/suspend")
    public void suspendProcess(@PathVariable String processInstanceId) {
        workflowService.suspendProcess(processInstanceId);
    }

    /**
     * Resumes the process instance with the given ID.
     *
     * @param processInstanceId the ID of the process instance
     */
    @PostMapping("/process/{processInstanceId}/resume")
    public void resumeProcess(@PathVariable String processInstanceId) {
        workflowService.resumeProcess(processInstanceId);
    }

    /**
     * Deletes the process instance with the given ID and reason.
     *
     * @param processInstanceId the ID of the process instance
     * @param reason reason for deletion
     */
    @DeleteMapping("/process/{processInstanceId}")
    public void deleteProcess(@PathVariable String processInstanceId, @RequestParam String reason) {
        workflowService.deleteProcess(processInstanceId, reason);
    }

    /**
     * Retrieves all variables for the given process instance.
     *
     * @param processInstanceId the ID of the process instance
     * @return map of variables
     */
    @GetMapping("/process/{processInstanceId}/variables")
    @ResponseBody
    public Map<String, Object> getProcessVariables(@PathVariable String processInstanceId) {
        return workflowService.getProcessVariables(processInstanceId);
    }

    /**
     * Sets a variable for the given process instance.
     *
     * @param processInstanceId the ID of the process instance
     * @param variableName the variable name
     * @param value the variable value
     */
    @PostMapping("/process/{processInstanceId}/variables")
    public void setProcessVariable(@PathVariable String processInstanceId, @RequestParam String variableName, @RequestParam String value) {
        workflowService.setProcessVariable(processInstanceId, variableName, value);
    }

    /**
     * Completes the task with the given ID and optional variables.
     *
     * @param taskId the ID of the task to complete
     * @param variables map of variables to include when completing the task
     */
    @PostMapping("/task/{taskId}/complete")
    public void completeTask(@PathVariable String taskId, @RequestBody Map<String, Object> variables) {
        workflowService.completeTask(taskId, variables);
    }

    /**
     * Retrieves all active tasks.
     *
     * @return list of task IDs
     */
    @GetMapping("/tasks")
    @ResponseBody
    public List<String> getAllActiveTasks() {
        return workflowService.getActiveTasks(null);
    }

    /**
     * Retrieves all running processes.
     *
     * @return list of running processes
     */
    @GetMapping("/process/running")
    @ResponseBody
    public Object getRunningProcesses() {
        return workflowService.getRunningProcesses();
    }

    /**
     * Retrieves all finished processes.
     *
     * @return list of finished process IDs
     */
    @GetMapping("/process/finished")
    @ResponseBody
    public List<String> getFinishedProcesses() {
        return workflowService.getFinishedProcesses();
    }
}