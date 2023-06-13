package com.gateway.services;

import java.util.Optional;

import com.gateway.models.Role;
import com.gateway.models.User;


public interface IUserService {
    User saveUser(User user);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    void changeRole(Role newRole, String username);

    void updteRole(Role newRole, Long id);

    User findByUsernameReturnToken(String username);
}
