package com.yyh.service;

import com.yyh.dto.User;

public interface UserService {
    User findUser(String userName);
    void addUser(User user);
}
// 자바 리플렉션과 cglib 차이 클래스를 프록시 할수 있을까요?
//public class UserService {
//    public User findUser(String userName){
//       return null;
//    }
//    public void addUser(User user){
//
//    }
//}
