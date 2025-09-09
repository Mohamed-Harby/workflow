package com.example.workflow.ui;

import com.example.workflow.service.WorkflowService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@PageTitle("Start | Workflow")
@Route(value = "ui/start", layout = MainLayout.class)
@PermitAll
public class StartView extends VerticalLayout {

    private final WorkflowService workflowService;

    private final TextField processKey = new TextField("Process Key");
    private final TextArea text = new TextArea("Initial Text (optional)");

    @Autowired
    public StartView(WorkflowService workflowService) {
        this.workflowService = workflowService;
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        processKey.setPlaceholder("e.g., simpleTextProcessingWorkflow");
        processKey.setRequired(true);
        processKey.setWidthFull();

        text.setPlaceholder("Enter initial text");
        text.setWidthFull();
        text.setMaxLength(2000);

        Button startBtn = new Button("Start Workflow", event -> startWorkflow());

        FormLayout form = new FormLayout();
        form.add(processKey, text, startBtn);
        form.setColspan(text, 2);

        add(form);
    }

    private void startWorkflow() {
        String key = processKey.getValue();
        if (key == null || key.isBlank()) {
            Notification.show("Process key is required", 3000, Notification.Position.MIDDLE);
            return;
        }
        Map<String, Object> vars = new HashMap<>();
        vars.put("text", text.getValue() != null ? text.getValue() : "");
        try {
            String processId = workflowService.startProcess(key, vars);
            Notification.show("Started process: " + processId, 4000, Notification.Position.BOTTOM_START);
            processKey.clear();
            text.clear();
        } catch (Exception ex) {
            Notification.show("Failed to start: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}
