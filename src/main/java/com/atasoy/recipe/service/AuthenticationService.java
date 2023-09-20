package com.atasoy.recipe.service;


import com.atasoy.recipe.dao.JwtAuthenticationResponse;
import com.atasoy.recipe.dao.SigninRequest;
import com.atasoy.recipe.dao.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<JwtAuthenticationResponse> signup(SignupRequest request);

    ResponseEntity<JwtAuthenticationResponse> signin(SigninRequest request);

    Boolean isTokenValid(String token);
}
