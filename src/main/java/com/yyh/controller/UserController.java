package com.yyh.controller;

import com.yyh.annotation.CustomAutowired;
import com.yyh.dto.User;
import com.yyh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

//    @Autowired
    @CustomAutowired
    private UserService userService;

    @GetMapping("/user/{userName}/{age}")
    public ResponseEntity addUser(@PathVariable("userName") String userName,
                        @PathVariable("age") Integer age) {
        User user = new User();
        user.setName(userName);
        user.setAge(age);

        userService.addUser(user);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity getUser(@PathVariable("userName") String userName) {
        final User user = userService.findUser(userName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
