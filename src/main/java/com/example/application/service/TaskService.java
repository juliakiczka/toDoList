package com.example.application.service;

import com.example.application.entity.Task;
import com.example.application.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    public Task add(Task task) {
        return repository.save(task);
    }

    public List<Task> getAll() {
        return repository.findAll();
    }

    public Task findByDescription(String description) {
        return repository
                .findAll()
                .stream()
                .filter(task -> task.getDescription().equals(description))
                .findFirst()
                .orElse(null);
    }

    public void delete(String description) {
        Task task = findByDescription(description);
        if (task != null) {
            repository.delete(task);
        }
    }
    public void deleteTask(Task task) {
        if (task != null) {
            repository.delete(task);
        }
    }


    public void deleteByID(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    public void updateTask(Task taskToUpdate) {
        Optional<Task> task = repository.findById(taskToUpdate.getId());
        task.ifPresent(value -> value.setDone(true));
        repository.save(task.get());
    }

    public Optional<Task> getTaskById(Long taskId) {
        return repository.findById(taskId);
    }
}
