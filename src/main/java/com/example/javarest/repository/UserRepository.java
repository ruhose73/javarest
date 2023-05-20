package com.example.javarest.repository;

import com.example.javarest.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

    UserEntity deleteByUsername(String username);

}
