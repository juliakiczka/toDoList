package com.example.application.views.main;

import com.example.application.entity.Task;
import com.example.application.service.TaskService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Route("")
@StyleSheet("/css/style.css")
@Slf4j

public class MainView extends VerticalLayout {
    private final TaskService service;

    @Autowired
    public MainView(TaskService service) {
        this.service = service;
    }

    @PostConstruct
    private void init() {
        log.info("Initializing MainView...");
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
            log.info("Add button clicked...");
            String taskDescription = taskField.getValue();
            if (!taskDescription.isEmpty()) {
                service.add(new Task(taskDescription));
                addTask(todosList, taskDescription);
                taskField.clear();
            } else {
                log.warn("Task field is empty!");
                Notification notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                Div text = new Div(new Text("Task field cannot be empty!"));
                Button closeButton = new Button(new Icon("lumo", "cross"));
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                closeButton.setAriaLabel("Close");
                closeButton.addClickListener(event -> {
                    notification.close();
                });
                HorizontalLayout layout = new HorizontalLayout(text, closeButton);
                layout.setAlignItems(Alignment.CENTER);
                notification.add(layout);
                notification.open();
            }

        });

        addButton.addClickShortcut(Key.ENTER);

        Button removeButton = getButton(todosList, tasks);

        add(
                new H1("Gurl's ToDoList"),
                new HorizontalLayout(
                        taskField,
                        addButton,
                        removeButton
                ),
                todosList


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
        log.info("Adding task to the list: {}", taskDescription);

        Checkbox checkbox = new Checkbox(taskDescription);
        checkbox.addValueChangeListener(checkboxClickEvent -> {
            Task byDescription = service.findByDescription(taskDescription);
            service.updateTask(byDescription);
            log.info("Checkbox value changed for task: {}", byDescription);
        });
        todosList.add(checkbox);
    }
}
