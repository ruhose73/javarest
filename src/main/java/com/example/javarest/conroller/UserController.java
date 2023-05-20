package com.example.javarest.conroller;

import com.example.javarest.entity.UserEntity;
import com.example.javarest.exceprion.TokenException;
import com.example.javarest.exceprion.UserAlreadyExistException;
import com.example.javarest.exceprion.UserNotFoundException;
import com.example.javarest.exceprion.WrongDataException;
import com.example.javarest.provider.JwtProvider;
import com.example.javarest.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    private final JwtProvider jwtProvider;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserEntity user) {
        try {
            return ResponseEntity.ok(userService.register(user));
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserEntity user) {
        try {
            return ResponseEntity.ok(userService.login(user));
        } catch (WrongDataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping()
    public ResponseEntity getOneUser(HttpServletRequest request) {
        try {
            Long userId = jwtProvider.getTokenFromRequest(request);
            return ResponseEntity.ok(userService.getOne(userId));
        } catch (TokenException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @DeleteMapping()
    public ResponseEntity deleteUser(HttpServletRequest request) {
        try {
            Long userId = jwtProvider.getTokenFromRequest(request);
            return ResponseEntity.ok(userService.delete(userId));
        } catch (TokenException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

}
