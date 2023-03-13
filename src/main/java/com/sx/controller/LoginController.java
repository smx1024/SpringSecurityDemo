package com.sx.controller;

import com.sx.domain.ResponseResult;
import com.sx.domain.User;
import com.sx.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    LoginService loginService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }
    @PostMapping("/user/logout")
    public ResponseResult logout(@RequestBody User user){
        return loginService.logout(user);
    }
}
