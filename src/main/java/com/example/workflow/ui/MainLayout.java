package com.example.workflow.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
        setPrimarySection(Section.DRAWER);
    }

    private void createHeader() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Workflow");
        title.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        // Right-side status bar segment (simple)
        HorizontalLayout status = new HorizontalLayout(new Icon(VaadinIcon.CHECK), new Span("Ready"));
        status.setAlignItems(FlexComponent.Alignment.CENTER);
        status.addClassNames(LumoUtility.Gap.XSMALL);

        HorizontalLayout header = new HorizontalLayout(toggle, title);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.expand(title);
        header.add(status);
        header.addClassNames(LumoUtility.Padding.SMALL);
        addToNavbar(header);
    }

    private void createDrawer() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Tasks", TasksView.class, VaadinIcon.TASKS.create()));
        nav.addItem(new SideNavItem("Start Workflow", StartView.class, VaadinIcon.PLAY.create()));

        addToDrawer(nav);
    }
}
