package com.example.javarest.conroller;

import com.example.javarest.entity.TodoEntity;
import com.example.javarest.exceprion.TodoNotFoundException;
import com.example.javarest.exceprion.TokenException;
import com.example.javarest.exceprion.UserNotFoundException;
import com.example.javarest.provider.JwtProvider;
import com.example.javarest.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {
    @Autowired
    private TodoService todoService;

    private final JwtProvider jwtProvider;

    @PostMapping
    public ResponseEntity createTodo(HttpServletRequest request, @RequestBody TodoEntity todo) {
        try {
            Long userId = jwtProvider.getTokenFromRequest(request);
            return ResponseEntity.ok(todoService.createTodo(todo, userId));
        } catch (TokenException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @PutMapping
    public ResponseEntity completeTodo(HttpServletRequest request, @RequestParam Long id) {
        try {
            jwtProvider.getTokenFromRequest(request);
            return ResponseEntity.ok(todoService.complete(id));
        } catch (TokenException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(HttpServletRequest request, @PathVariable Long id) {
        try {
            jwtProvider.getTokenFromRequest(request);
            return ResponseEntity.ok(todoService.delete(id));
        } catch (TokenException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (
                Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }
}
