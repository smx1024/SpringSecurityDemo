package com.sx.service.impl;

import com.sx.domain.LoginUser;
import com.sx.domain.ResponseResult;
import com.sx.domain.User;
import com.sx.service.LoginService;
import com.sx.util.JwtUtil;
import com.sx.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        //进行用户验证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //认证没通过，提示错误
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("密码错误");
        }
        //如果认证通过。使用Userid生成jwt，jwt存入responseresult返回
        LoginUser loginUser= (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        Map<String,String> map=new HashMap<>();
        map.put("token",jwt);
        //将信息存入redis，userid作为key
        redisCache.setCacheObject("login:"+ userId,loginUser);
        return new  ResponseResult(200,"登录成功",map);
    }

    @Override
    public ResponseResult logout(User user) {
        //SecurityContextHold中的用户ID
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUser().getId();
        redisCache.deleteObject("login:"+id);
        return  new ResponseResult(200,"注销成功");
    }
}
