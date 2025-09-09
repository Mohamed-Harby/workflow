package com.example.workflow.controller;

import com.example.workflow.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/ui/processes")
@RequiredArgsConstructor
public class ProcessUIController {

    private final WorkflowService workflowService;

    /**
     * Show a form to start a process instance.
     */
    @GetMapping("/start")
    public String showStartForm(Model model) {
        // Add some common process keys for selection (in real app you might fetch them dynamically)
        model.addAttribute("processKeys", new String[]{"simpleTextProcessingWorkflow", "anotherProcess"});
        return "process-start";
    }

    /**
     * Handle starting a process instance.
     */
    @PostMapping("/start")
    public String startProcess(@RequestParam String processKey,
                               @RequestParam Map<String, Object> variables) {
        workflowService.startProcess(processKey, variables);
        return "redirect:/ui/tasks"; // after starting, redirect to tasks page
    }
}