package com.example.javarest.service;

import com.example.javarest.entity.UserEntity;
import com.example.javarest.exceprion.UserAlreadyExistException;
import com.example.javarest.exceprion.UserNotFoundException;
import com.example.javarest.model.User;
import com.example.javarest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register (UserEntity user) throws UserAlreadyExistException {
        if(userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistException("Пользователь уже существует");
        }
        return User.toModel(userRepository.save(user));
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
