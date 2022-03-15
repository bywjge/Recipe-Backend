package com.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.recipe.entity.User;
import com.recipe.repository.UserRepository;

@Service
public class RegisterService {

    @Autowired
    UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public User findUserByEmail(String username) {
        return userRepository.findUserByUsername(username);
    }


}
