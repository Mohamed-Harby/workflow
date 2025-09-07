package com.example.workflow.controller;

import com.example.workflow.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.flowable.engine.TaskService;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;
    private final TaskService taskService;

    // Show a simple start page
    @GetMapping("/start/{processKey}")
    public String startProcess(@PathVariable String processKey, Model model) {
        String processId = workflowService.startProcess(processKey, Map.of("text", "Hello Flowable!"));
        model.addAttribute("processId", processId);
        model.addAttribute("activePage", "start");
        return "workflow-start"; // Thymeleaf template (resources/templates/workflow-start.html)
    }

    // Complete a task and then show remaining tasks
    @PostMapping("/task/{taskId}/complete")
    public String completeTask(@PathVariable String taskId, @RequestParam(required = false) String text, Model model) {
        workflowService.completeTask(taskId, Map.of("text", text != null ? text : "Hello from User!"));

        // After completing, redirect to the tasks page
        return "redirect:/workflow/tasks";
    }

    // Show active tasks
    @GetMapping("/tasks")
    public String getTasks(@RequestParam(required = false) String processInstanceId, Model model) {
        List<org.flowable.task.api.Task> tasks;
        if (processInstanceId != null) {
            // Get task objects instead of just IDs
            tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        } else {
            tasks = workflowService.getAllActiveTasks();
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("activePage", "tasks");
        return "workflow-tasks";
    }

    @GetMapping("/start")
    public String showStartForm(Model model) {
        model.addAttribute("activePage", "start");
        return "workflow-start-form"; // Thymeleaf template
    }

    @PostMapping("/start")
    public String handleStartForm(@RequestParam String processKey, @RequestParam(required = false) String text) {
        // Start the process with the provided key and text
        String processId = workflowService.startProcess(processKey, Map.of("text", text != null ? text : ""));
        return "redirect:/workflow/start/" + processKey + "?processId=" + processId;
    }
}