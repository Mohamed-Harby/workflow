package com.example.workflow.ui;

import com.example.workflow.service.WorkflowService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Tasks | Workflow")
@Route(value = "ui/tasks", layout = MainLayout.class)
@PermitAll
public class TasksView extends VerticalLayout {

    private final WorkflowService workflowService;

    private final Grid<Task> grid = new Grid<>(Task.class, false);
    private final TextField filterByAssignee = new TextField("Filter by assignee");

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

    @Autowired
    public TasksView(WorkflowService workflowService) {
        this.workflowService = workflowService;
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        configureGrid();

        Button refresh = new Button("Refresh", new Icon(VaadinIcon.REFRESH));
        refresh.addClickListener(e -> loadData());

        HorizontalLayout actions = new HorizontalLayout(filterByAssignee, refresh);
        actions.setWidthFull();
        filterByAssignee.addValueChangeListener(e -> loadData());

        add(actions, grid);
        expand(grid);

        loadData();
    }

    private void configureGrid() {
        grid.addColumn(Task::getName).setHeader("Name").setAutoWidth(true).setFlexGrow(2);
        grid.addColumn(Task::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(t -> t.getAssignee() != null ? t.getAssignee() : "Unassigned").setHeader("Assignee").setAutoWidth(true);
        grid.addColumn(Task::getPriority).setHeader("Priority").setAutoWidth(true);
        grid.addColumn(t -> t.getCreateTime() != null ?
                t.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).format(fmt) : "").setHeader("Created").setAutoWidth(true);

        grid.addColumn(new ComponentRenderer<>(task -> {
            Button complete = new Button("Complete", new Icon(VaadinIcon.CHECK));
            complete.addClickListener(e -> completeTask(task));
            complete.getElement().setProperty("theme", "primary");
            return complete;
        })).setHeader("Actions").setAutoWidth(true).setFlexGrow(0);

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);
    }

    private void loadData() {
        List<Task> tasks = workflowService.getAllActiveTasks();
        String assignee = filterByAssignee.getValue();
        if (assignee != null && !assignee.isBlank()) {
            tasks = tasks.stream().filter(t -> assignee.equalsIgnoreCase(String.valueOf(t.getAssignee()))).toList();
        }
        grid.setItems(tasks);
    }

    private void completeTask(Task task) {
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("text", "Completed via Vaadin UI");
            workflowService.completeTask(task.getId(), vars);
            Notification.show("Completed task " + task.getName(), 3000, Notification.Position.BOTTOM_START);
            loadData();
        } catch (Exception ex) {
            Notification.show("Failed to complete: " + ex.getMessage(), 4000, Notification.Position.MIDDLE);
        }
    }
}
