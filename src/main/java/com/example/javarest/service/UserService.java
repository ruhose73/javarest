package com.example.javarest.service;

import com.example.javarest.entity.UserEntity;
import com.example.javarest.exceprion.UserAlreadyExistException;
import com.example.javarest.exceprion.UserNotFoundException;
import com.example.javarest.exceprion.WrongDataException;
import com.example.javarest.model.JwtResponse;
import com.example.javarest.model.User;
import com.example.javarest.provider.JwtProvider;
import com.example.javarest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public JwtResponse register (UserEntity user) throws UserAlreadyExistException {
        if(userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistException("Пользователь уже существует");
        }
        userRepository.save(user);
        return new JwtResponse(jwtProvider.generateAccessToken(user));
    }

    public JwtResponse login (UserEntity user) throws WrongDataException {
        UserEntity userData =  userRepository.findByUsername(user.getUsername());
        if(!Objects.equals(userData.getUsername(), user.getUsername()) || !Objects.equals(userData.getPassword(), user.getPassword())) {
            throw new WrongDataException("Неверные данные");
        }
        System.out.println(1);
        return new JwtResponse(jwtProvider.generateAccessToken(user));
    }

    public User getOne(Long id) {
        UserEntity user = userRepository.findById(id).get();
        return User.toModel(user);
    }

    public Long delete(Long id) throws UserNotFoundException {
        userRepository.deleteById(id);
        return id;
    }
}
