package com.atasoy.recipe.service.impl;

import com.atasoy.recipe.dao.SignupRequest;
import com.atasoy.recipe.entity.User;
import com.atasoy.recipe.repo.UserRepository;
import com.atasoy.recipe.service.UserService;
import jakarta.validation.ValidationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByUserNameAndDeletedFalse(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    public void validateSignupRequest(SignupRequest request) throws ValidationException {
        if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
            throw new ValidationException("First name is required");
        }

        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            throw new ValidationException("Last name is required");
        }

        if (request.getUserName() == null || request.getUserName().isEmpty()) {
            throw new ValidationException("Username is required");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new ValidationException("Password is required");
        }

        if (userRepository.existsByUserName(request.getUserName())) {
            throw new ValidationException("Username is already taken");
        }

    }

    public User getUserFromSession() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return null;

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String userName = ((UserDetails) principal).getUsername();
            return userRepository.findByUserNameAndDeletedFalse(userName).orElse(null);
        } else {
            User guest = new User();
            guest.setId(-1);
            return guest;
        }
    }
}
