package com.atasoy.recipe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    @GetMapping
    public ResponseEntity<String> greetings() {
        return ResponseEntity.ok("you have reached the safe endpoint");
    }
}
