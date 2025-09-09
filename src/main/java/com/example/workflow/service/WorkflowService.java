package com.example.workflow.service;

import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.List;
import java.util.Map;

public interface WorkflowService {

    /**
     * Starts a new process instance with the given process key and variables.
     *
     * @param processKey the key of the process definition
     * @param variables  the variables to start the process with
     */
    void startProcess(String processKey, Map<String, Object> variables);

    /**
     * Suspends the process instance identified by the given process instance ID.
     *
     * @param processInstanceId the ID of the process instance to suspend
     */
    void suspendProcess(String processInstanceId);

    /**
     * Resumes a suspended process instance identified by the given process instance ID.
     *
     * @param processInstanceId the ID of the process instance to resume
     */
    void resumeProcess(String processInstanceId);

    /**
     * Deletes the process instance identified by the given process instance ID with a specified reason.
     *
     * @param processInstanceId the ID of the process instance to delete
     * @param reason            the reason for deleting the process instance
     */
    void deleteProcess(String processInstanceId, String reason);

    /**
     * Retrieves the process instance identified by the given process instance ID.
     *
     * @param processInstanceId the ID of the process instance to retrieve
     * @return the process instance, or null if not found
     */
    ProcessInstance getProcessInstance(String processInstanceId);

    /**
     * Completes the task identified by the given task ID, optionally setting variables.
     *
     * @param taskId    the ID of the task to complete
     * @param variables the variables to set upon task completion
     */
    void completeTask(String taskId, Map<String, Object> variables);

    /**
     * Claims the task identified by the given task ID for the specified user.
     *
     * @param taskId the ID of the task to claim
     * @param userId the ID of the user claiming the task
     */
    void claimTask(String taskId, String userId);

    /**
     * Unclaims the task identified by the given task ID, making it available for others.
     *
     * @param taskId the ID of the task to unclaim
     */
    void unclaimTask(String taskId);

    /**
     * Assigns the task identified by the given task ID to the specified user.
     *
     * @param taskId the ID of the task to assign
     * @param userId the ID of the user to assign the task to
     */
    void assignTask(String taskId, String userId);

    /**
     * Retrieves a list of active task IDs for the specified process instance.
     *
     * @param processInstanceId the ID of the process instance
     * @return a list of active task IDs
     */
    List<String> getActiveTasks(String processInstanceId);

    /**
     * Retrieves all active tasks across all process instances.
     *
     * @return a list of all active tasks
     */
    List<Task> getAllActiveTasks();

    /**
     * Retrieves tasks assigned to the specified assignee.
     *
     * @param assignee the user ID of the assignee
     * @return a list of tasks assigned to the user
     */
    List<Task> getTasksByAssignee(String assignee);

    /**
     * Retrieves tasks available to the specified candidate group.
     *
     * @param group the candidate group name
     * @return a list of tasks for the candidate group
     */
    List<Task> getTasksByCandidateGroup(String group);

    /**
     * Retrieves the task identified by the given task ID.
     *
     * @param taskId the ID of the task to retrieve
     * @return the task, or null if not found
     */
    Task getTaskById(String taskId);

    /**
     * Retrieves all variables associated with the specified process instance.
     *
     * @param processInstanceId the ID of the process instance
     * @return a map of process variables
     */
    Map<String, Object> getProcessVariables(String processInstanceId);

    /**
     * Sets a variable for the specified process instance.
     *
     * @param processInstanceId the ID of the process instance
     * @param variableName      the name of the variable to set
     * @param value             the value of the variable
     */
    void setProcessVariable(String processInstanceId, String variableName, Object value);

    /**
     * Retrieves all variables associated with the specified task.
     *
     * @param taskId the ID of the task
     * @return a map of task variables
     */
    Map<String, Object> getTaskVariables(String taskId);

    /**
     * Sets a variable for the specified task.
     *
     * @param taskId       the ID of the task
     * @param variableName the name of the variable to set
     * @param value        the value of the variable
     */
    void setTaskVariable(String taskId, String variableName, Object value);

    /**
     * Retrieves all currently running process instances.
     *
     * @return a list of running process instances
     */
    List<ProcessInstance> getRunningProcesses();

    /**
     * Retrieves the IDs of all finished process instances.
     *
     * @return a list of finished process instance IDs
     */
    List<String> getFinishedProcesses();
}