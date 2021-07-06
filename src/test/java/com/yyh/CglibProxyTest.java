package com.yyh;

import com.yyh.dto.User;
import com.yyh.service.UserService;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CglibProxyTest {
    @Test
    public void test() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserService.class);
        enhancer.setCallback(new InvocationHandler() {

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
        UserService userService = (UserService) enhancer.create();

        User user = new User();
        user.setName("유영훈");
        user.setAge(30);
        userService.addUser(user);

        System.out.println("userService = " + userService.findUser("유영훈").toString());
    }
}
