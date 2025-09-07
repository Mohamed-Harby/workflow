package com.example.workflow.service.implementation;

import com.example.workflow.service.WorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class FlowableWorkflowService implements WorkflowService {
    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public FlowableWorkflowService(RuntimeService runtimeService, TaskService taskService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }

    @Override
    public String startProcess(String processKey, Map<String, Object> variables) {
        log.info("Starting process with key: {} and variables: {}", processKey, variables);
        String processInstanceId = runtimeService.startProcessInstanceByKey(processKey, variables).getId();
        log.info("Started process instance with ID: {}", processInstanceId);
        return processInstanceId;
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        log.info("Completing task with ID: {} and variables: {}", taskId, variables);
        taskService.complete(taskId, variables);
        log.info("Task {} completed successfully", taskId);
    }

    @Override
    public List<String> getActiveTasks(String processInstanceId) {
        log.info("Fetching active tasks for processInstanceId: {}", processInstanceId);
        List<String> taskIds = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        log.info("Active tasks for processInstanceId {}: {}", processInstanceId, taskIds);
        return taskIds;
    }

    public List<Task> getAllActiveTasks() {
        return taskService.createTaskQuery()
                .active()
                .list();
    }

}
