package com.daniel.dev.controllers;

import com.daniel.dev.dto.EmailDTO;
import com.daniel.dev.dto.NewPasswordDTO;
import com.daniel.dev.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/recover-token")
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailDTO obj) {
        authService.createRecoverToken(obj);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/new-password")
    public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO obj) {
        authService.saveNewPassword(obj);
        return ResponseEntity.noContent().build();
    }
}