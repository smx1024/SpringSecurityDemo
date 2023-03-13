package com.sx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sx.domain.LoginUser;
import com.sx.domain.User;
import com.sx.mapper.MenuMapper;
import com.sx.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserMapper userMapper ;
    @Autowired
    MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String usename) throws UsernameNotFoundException {

        //查询用户信息
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,usename);
        User user = userMapper.selectOne(queryWrapper);
        //TODO 查询对应权限信息
        List<String> list=menuMapper.selectPermsByUserId(user.getId());
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或者密码错误");
        }
        return new LoginUser(user,list);
    }
}
