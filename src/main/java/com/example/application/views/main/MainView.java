package com.example.application.views.main;

import com.example.application.entity.Task;
import com.example.application.service.TaskService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("")
@StyleSheet("/css/style.css")

public class MainView extends VerticalLayout {
    private final TaskService service;

    @Autowired

    public MainView(TaskService service) {
        this.service = service;
    }

    @PostConstruct
    private void init() {
        VerticalLayout todosList = new VerticalLayout();
        TextField taskField = new TextField();
        Button addButton = new Button("Add");
        Task task = new Task();
        List<Task> tasks = service.getAll();
        for (Task element : tasks) {
            Checkbox checkbox = new Checkbox(element.getTask());
            todosList.add(checkbox);
        }
        addButton.addClickListener(click -> {
            Checkbox checkbox = new Checkbox(taskField.getValue());
            service.add(new Task(taskField.getValue()));
            todosList.add(checkbox);
            taskField.clear();
        });
        addButton.addClickShortcut(Key.ENTER);

        add(
                new H1("Gurl's ToDoList"),
                todosList,
                new HorizontalLayout(
                        taskField,
                        addButton
                )
        );
    }
}