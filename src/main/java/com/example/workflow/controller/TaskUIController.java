package com.example.workflow.controller;

import com.example.workflow.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/ui/tasks")
@RequiredArgsConstructor
public class TaskUIController {

    private final WorkflowService workflowService;

    /**
     * Shows a page with all active tasks.
     */
    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("tasks", workflowService.getAllActiveTasks());
        return "tasks"; // Thymeleaf template: tasks.html
    }

    /**
     * Completes a task.
     */
    @PostMapping("/{taskId}/complete")
    public String completeTask(@PathVariable String taskId, @RequestParam Map<String, Object> variables) {
        workflowService.completeTask(taskId, variables);
        return "redirect:/ui/tasks";
    }
}