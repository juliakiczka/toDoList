package com.example.application.service;

import com.example.application.entity.Task;
import com.example.application.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;
    public Task add(Task task){
        return repository.save(task);
    }
    public List<Task> getAll(){
        return repository.findAll();
    }
}
