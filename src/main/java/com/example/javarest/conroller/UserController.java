package com.example.javarest.conroller;

import com.example.javarest.entity.UserEntity;
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

import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    private final JwtProvider jwtProvider;

    private String getTokenFromRequest(HttpServletRequest request) {
        final String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            jwtProvider.validateAccessToken(jwtToken);
            Claims claims = jwtProvider.getAccessClaims(jwtToken);
            System.out.println(claims);
            return String.valueOf(claims.get("username"));
        }
        return null;
    }

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
    public ResponseEntity login(HttpServletRequest request, @RequestBody UserEntity user) {
        try {
            String username = getTokenFromRequest(request);
            if(!(username.equals(user.getUsername()))) {
                return ResponseEntity.badRequest().body("Не валидный токен");
            }
            return ResponseEntity.ok(userService.login(user));
        } catch (WrongDataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping
    public ResponseEntity getOneUser(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(userService.getOne(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.delete(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

}
