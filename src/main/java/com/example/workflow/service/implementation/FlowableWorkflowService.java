package com.example.workflow.service.implementation;

import com.example.workflow.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class FlowableWorkflowService implements WorkflowService {
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;

    @Override
    public void startProcess(String processKey, Map<String, Object> variables) {
        log.info("Starting process with key: [{}] and variables: [{}]", processKey, variables);
        String processInstanceId = runtimeService.startProcessInstanceByKey(processKey, variables).getId();
        log.info("Started process instance with ID: [{}]", processInstanceId);
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        log.info("Completing task with ID: [{}] and variables: [{}]", taskId, variables);
        taskService.complete(taskId, variables);
        log.info("Task [{}] completed successfully", taskId);
    }

    @Override
    public List<String> getActiveTasks(String processInstanceId) {
        log.info("Fetching active tasks for processInstanceId: [{}]", processInstanceId);
        List<String> taskIds = taskService.createTaskQuery().processInstanceId(processInstanceId).list().stream().map(Task::getId).collect(Collectors.toList());
        log.info("Active tasks for processInstanceId [{}]: [{}]", processInstanceId, taskIds);
        return taskIds;
    }

    public List<Task> getAllActiveTasks() {
        log.info("Fetching all active tasks");
        List<Task> result = taskService.createTaskQuery().active().list();
        log.info("Total active tasks: [{}]", result.size());
        return result;
    }

    @Override
    public void suspendProcess(String processInstanceId) {
        log.info("Suspending process instance: [{}]", processInstanceId);
        runtimeService.suspendProcessInstanceById(processInstanceId);
        log.info("Process [{}] suspended", processInstanceId);
    }

    @Override
    public void resumeProcess(String processInstanceId) {
        log.info("Resuming process instance: [{}]", processInstanceId);
        runtimeService.activateProcessInstanceById(processInstanceId);
        log.info("Process [{}] resumed", processInstanceId);
    }

    @Override
    public void deleteProcess(String processInstanceId, String reason) {
        log.info("Deleting process instance: [{}] with reason: [{}]", processInstanceId, reason);
        runtimeService.deleteProcessInstance(processInstanceId, reason);
        log.info("Process [{}] deleted", processInstanceId);
    }

    @Override
    public ProcessInstance getProcessInstance(String processInstanceId) {
        log.info("Fetching process instance with ID: [{}]", processInstanceId);
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        log.info("Process instance [{}] fetched successfully", processInstanceId);
        return pi;
    }

    @Override
    public void claimTask(String taskId, String userId) {
        log.info("Claiming task [{}] for user [{}]", taskId, userId);
        taskService.claim(taskId, userId);
        log.info("Task [{}] claimed by [{}]", taskId, userId);
    }

    @Override
    public void unclaimTask(String taskId) {
        log.info("Unclaiming task [{}]", taskId);
        taskService.unclaim(taskId);
        log.info("Task [{}] unclaimed", taskId);
    }

    @Override
    public void assignTask(String taskId, String userId) {
        log.info("Assigning task [{}] to user [{}]", taskId, userId);
        taskService.setAssignee(taskId, userId);
        log.info("Task [{}] assigned to [{}]", taskId, userId);
    }

    @Override
    public List<Task> getTasksByAssignee(String assignee) {
        log.info("Fetching tasks for assignee [{}]", assignee);
        List<Task> result = taskService.createTaskQuery().taskAssignee(assignee).list();
        log.info("Found [{}] tasks for assignee [{}]", result.size(), assignee);
        return result;
    }

    @Override
    public List<Task> getTasksByCandidateGroup(String group) {
        log.info("Fetching tasks for candidate group [{}]", group);
        List<Task> result = taskService.createTaskQuery().taskCandidateGroup(group).list();
        log.info("Found [{}] tasks for candidate group [{}]", result.size(), group);
        return result;
    }

    @Override
    public Task getTaskById(String taskId) {
        log.info("Fetching task by ID [{}]", taskId);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            log.info("Task [{}] found", taskId);
        } else {
            log.info("Task [{}] not found", taskId);
        }
        return task;
    }

    @Override
    public Map<String, Object> getProcessVariables(String processInstanceId) {
        log.info("Fetching variables for process [{}]", processInstanceId);
        Map<String, Object> vars = runtimeService.getVariables(processInstanceId);
        log.info("Process [{}] variables fetched: [{}]", processInstanceId, vars.keySet());
        return vars;
    }

    @Override
    public void setProcessVariable(String processInstanceId, String variableName, Object value) {
        log.info("Setting variable [{}] for process [{}] with value [{}]", variableName, processInstanceId, value);
        runtimeService.setVariable(processInstanceId, variableName, value);
        log.info("Process variable [{}] set for process [{}]", variableName, processInstanceId);
    }

    @Override
    public Map<String, Object> getTaskVariables(String taskId) {
        log.info("Fetching variables for task [{}]", taskId);
        Map<String, Object> vars = taskService.getVariables(taskId);
        log.info("Task [{}] variables fetched: [{}]", taskId, vars.keySet());
        return vars;
    }

    @Override
    public void setTaskVariable(String taskId, String variableName, Object value) {
        log.info("Setting variable [{}] for task [{}] with value [{}]", variableName, taskId, value);
        taskService.setVariable(taskId, variableName, value);
        log.info("Task variable [{}] set for task [{}]", variableName, taskId);
    }

    @Override
    public List<ProcessInstance> getRunningProcesses() {
        log.info("Fetching all running processes");
        List<ProcessInstance> result = runtimeService.createProcessInstanceQuery().active().list();
        log.info("Total running processes: [{}]", result.size());
        return result;
    }

    @Override
    public List<String> getFinishedProcesses() {
        log.info("Fetching all finished processes");
        List<String> result = historyService.createHistoricProcessInstanceQuery().finished().list().stream().peek(h -> log.debug("Finished process instance: [{}]", h.getId())).map(h -> h.getId()).collect(Collectors.toList());
        log.info("Total finished processes: [{}]", result.size());
        return result;
    }

}
