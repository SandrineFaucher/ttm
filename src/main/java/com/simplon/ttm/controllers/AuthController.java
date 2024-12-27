

package com.simplon.ttm.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.ttm.config.JwtAuthResponse;
import com.simplon.ttm.dto.LoginDto;
import com.simplon.ttm.services.AuthService;



import lombok.AllArgsConstructor;

@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class AuthController {

    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccesToken(token);
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }
}






