package com.example.javarest.service;

import com.example.javarest.entity.TodoEntity;
import com.example.javarest.entity.UserEntity;
import com.example.javarest.exceprion.TodoNotFoundException;
import com.example.javarest.exceprion.UserNotFoundException;
import com.example.javarest.model.Todo;
import com.example.javarest.repository.TodoRepository;
import com.example.javarest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserRepository userRepository;

    public Todo createTodo(TodoEntity todo, Long userId) {
        UserEntity user = userRepository.findById(userId).get();
        todo.setUser(user);
        return Todo.toModel(todoRepository.save(todo));
    }

    public Todo complete(Long id) {
        TodoEntity todo = todoRepository.findById(id).get();
        todo.setCompleted(!todo.getCompleted());
        return Todo.toModel(todoRepository.save(todo));
    }

    public Long delete(Long id) throws TodoNotFoundException {
        todoRepository.deleteById(id);
        return id;
    }

}