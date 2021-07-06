package com.yyh.service.impl;

import com.yyh.annotation.CustomService;
import com.yyh.dto.User;
import com.yyh.service.UserService;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//public class UserServiceImp extends UserService {
@CustomService
public class UserServiceImp implements UserService {

    private List<User> userList = new ArrayList();

    @Override
    public User findUser(String userName) {
        return userList.stream()
                .filter(user -> Objects.equals(userName, user.getName()))
                .findFirst().orElse(null);
    }

    @Override
    public void addUser(User user) {
        userList.add(user);
    }

}
