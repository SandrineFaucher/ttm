package com.simplon.ttm.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.simplon.ttm.dto.RegisterDto;
import com.simplon.ttm.services.UserService;

import jakarta.validation.Valid;

@Controller
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Méthode qui permet à l'admin de l'association de créer un user en fonction de son role
     * @param userMapping
     * @return user with role
     */
    @PostMapping("/register/save")
    public String registerUser(@Valid @ModelAttribute RegisterDto userMapping) {
        System.out.println("Ok");
        if(!userMapping.getPassword().equals(userMapping.getPasswordConfirm())){
            return "redirect:/register?error";
        }
        userService.saveUser(userMapping);
        return "redirect:/register/success";
    }
}
