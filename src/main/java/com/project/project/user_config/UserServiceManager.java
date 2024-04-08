package com.project.project.user_config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class UserServiceManager {

    @Autowired
    private UserRepository userRepository;

    /**
     * The Add(User) adds a new user in the DB.
     * @param user user.
     */
    public void Add(User user) {
        if(userRepository.existsById(user.getUsername()))
            return;
        userRepository.save(user);
    }

    /**
     * The GetById(String) gets a user by id.
     * @param username username.
     * @return user, if user exists in the DB.
     */
    public User GetById(String username) {
        return userRepository
                .findById(username)
                .get();
    }

    /**
     * The IsExist(String) checks a user in the DB.
     * @param username username.
     * @return true if user exists.
     */
    public boolean IsExist(String username) {
        return userRepository.existsById(username);
    }

    /**
     * The UserDetailsService() is a service data explorer.
     * @return link to the UserDetailsService method.
     */
    public UserDetailsService UserDetailsService() {
        return this::GetById;
    }
}
