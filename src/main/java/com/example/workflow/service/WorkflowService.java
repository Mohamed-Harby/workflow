package com.example.workflow.service;


import org.flowable.task.api.Task;

import java.util.List;
import java.util.Map;

public interface WorkflowService {
    String startProcess(String processKey, Map<String, Object> variables);

    void completeTask(String taskId, Map<String, Object> variables);

    List<String> getActiveTasks(String processInstanceId);

    List<Task> getAllActiveTasks();
}
