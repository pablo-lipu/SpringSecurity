package com.gateway.services;

import com.gateway.models.User;

public interface IAuthenticationService {
    public User signInAndReturnJWT(User signInRequest);
}
