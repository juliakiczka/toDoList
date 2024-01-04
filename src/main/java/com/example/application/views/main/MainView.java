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

import java.util.*;


@Route("")
@StyleSheet("/css/style.css")
public class MainView extends VerticalLayout {
    private final TaskService service;
    private final Map<Checkbox, Task> checkboxTaskMap = new HashMap<>();


    @Autowired
    public MainView(TaskService service) {
        this.service = service;
    }

    @PostConstruct
    private void init() {
        VerticalLayout todosList = new VerticalLayout();
        TextField taskField = new TextField();
        Button addButton = new Button("Add");
        addButton.addClassName("custom-color");
        Task task = new Task();
        List<Task> tasks = service.getAll();

        for (Task element : tasks) {
            addTask(todosList, element.getDescription());
        }

        addButton.addClickListener(click -> {
            String taskDescription = taskField.getValue();
            service.add(new Task(taskDescription));
            addTask(todosList, taskDescription);
            taskField.clear();
        });

        addButton.addClickShortcut(Key.ENTER);

        Button removeButton = getButton(todosList, tasks);

        add(
                new H1("Gurl's ToDoList"),
                todosList,
                new HorizontalLayout(
                        taskField,
                        addButton,
                        removeButton
                )
        );
    }

    private Button getButton(VerticalLayout todosList, List<Task> tasks) {
        Button removeButton = new Button("Remove Checked");
        removeButton.addClassName("custom-color");
        removeButton.addClickListener(click -> {
            todosList.getChildren()
                    .filter(Checkbox.class::isInstance)
                    .map(Checkbox.class::cast)
                    .filter(Checkbox -> Boolean.parseBoolean(Checkbox.getElement().getProperty("checked")))
                    .forEach(checkbox -> {
                        tasks
                                .stream()
                                .filter(Task::isDone)
                                .forEach(service::deleteTask);
                        todosList.remove(checkbox);
                    });

        });
        return removeButton;
    }
    private void addTask(VerticalLayout todosList, String taskDescription) {
        Checkbox checkbox = new Checkbox(taskDescription);
        todosList.add(checkbox);
    }
}
