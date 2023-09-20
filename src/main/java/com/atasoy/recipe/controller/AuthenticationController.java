package com.atasoy.recipe.controller;

import com.atasoy.recipe.dao.JwtAuthenticationResponse;
import com.atasoy.recipe.dao.SigninRequest;
import com.atasoy.recipe.dao.SignupRequest;
import com.atasoy.recipe.service.AuthenticationService;
import com.atasoy.recipe.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request).getBody());
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request).getBody());
    }

    @PostMapping("/is-token-valid")
    public ResponseEntity<Boolean> isTokenValid(@RequestBody String token) {
        return ResponseEntity.ok(authenticationService.isTokenValid(token));
    }
}
