package com.atasoy.recipe.service.impl;

import com.atasoy.recipe.dao.JwtAuthenticationResponse;
import com.atasoy.recipe.dao.SigninRequest;
import com.atasoy.recipe.dao.SignupRequest;
import com.atasoy.recipe.entity.User;
import com.atasoy.recipe.entity.enums.Role;
import com.atasoy.recipe.mapper.UserMapper;
import com.atasoy.recipe.repo.UserRepository;
import com.atasoy.recipe.service.AuthenticationService;
import com.atasoy.recipe.service.JwtService;
import com.atasoy.recipe.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                     JwtService jwtService, UserService userService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<JwtAuthenticationResponse> signup(@Valid SignupRequest request) {

        try {
            String firstName = request.getFirstName();
            String lastName = request.getLastName();
            String userName = request.getUserName();
            userService.validateSignupRequest(request);
            var user = new User(firstName, lastName,
                    userName, passwordEncoder.encode(request.getPassword()),
                    Role.ROLE_USER);
            userRepository.save(user);

            var jwtToken = jwtService.generateToken(user);

            JwtAuthenticationResponse response = new JwtAuthenticationResponse(true, null, "Kayıt başarılı!", UserMapper.toUserModel(user));

            return ResponseEntity.ok(response);
        } catch (ValidationException e) {
            if (e.getMessage().equals("Username is already taken")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new JwtAuthenticationResponse(false, "", e.getMessage(), null));
            } else {
                return ResponseEntity.badRequest().body(new JwtAuthenticationResponse(false, "", e.getMessage(), null));
            }
        } catch (DataIntegrityViolationException ex) {
            // If there's a data integrity violation, it means the username is already taken
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new JwtAuthenticationResponse(false, "", "Username is already taken", null));
        }
    }


    public ResponseEntity<JwtAuthenticationResponse> signin(SigninRequest request) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUserName(), request.getPassword()
            ));

            var user = userRepository.findByUserNameAndDeletedFalse(request.getUserName())
                    .orElseThrow(() -> new IllegalArgumentException("User not found."));
            var jwtToken = jwtService.generateToken(user);
            JwtAuthenticationResponse response = new JwtAuthenticationResponse(true, jwtToken, "", UserMapper.toUserModel(user));
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            // Handle authentication failure and return a meaningful response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtAuthenticationResponse(false, "", "Invalid username or password", null));
        }
    }

    public Boolean isTokenValid(String token) {
        UserDetails userDetails = getUserDetailsFromToken(token);
        return userDetails != null && !isTokenExpired(token);
    }

    private UserDetails getUserDetailsFromToken(String token) {
        String userName = jwtService.extractUserName(token);
        return userService.userDetailsService().loadUserByUsername(userName);
    }

    private boolean isTokenExpired(String token) {
        return jwtService.isTokenExpired(token);
    }

}
