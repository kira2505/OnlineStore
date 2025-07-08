package com.telran.store.controller;

import com.telran.store.dto.LoginRequestDto;
import com.telran.store.service.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private AuthenticationService authenticationService;

//    @PostMapping("/login")
//    public String login(@RequestBody LoginRequestDto loginRequestDto) {
//           return authenticationService.login(loginRequestDto);
//    }
//}
