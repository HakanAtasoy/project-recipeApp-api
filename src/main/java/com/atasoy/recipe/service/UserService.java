package com.atasoy.recipe.service;

import com.atasoy.recipe.dao.SignupRequest;
import com.atasoy.recipe.entity.User;
import jakarta.validation.ValidationException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();

    public void validateSignupRequest(SignupRequest request) throws ValidationException;

    public User getUserFromSession();

}
