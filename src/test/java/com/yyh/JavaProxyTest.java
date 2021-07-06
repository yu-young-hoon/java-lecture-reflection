package com.yyh;

import com.yyh.dto.User;
import com.yyh.service.UserService;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JavaProxyTest {
    @Test
    public void 테스트01() {
        UserService userService = (UserService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[] {UserService.class},
                new InvocationHandler() {

                    private List<User> userList = new ArrayList();

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return method.invoke(new UserService() {

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
                        }, args);
                    }
                });

        User user = new User();
        user.setName("유영훈");
        user.setAge(30);
        userService.addUser(user);

        System.out.println("userService = " + userService.findUser("유영훈").toString());
    }
}