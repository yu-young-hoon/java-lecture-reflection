package com.yyh.util;

import com.yyh.annotation.CustomAutowired;
import com.yyh.dto.User;
import com.yyh.service.UserService;
import net.sf.cglib.proxy.Enhancer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {

    private final Class controllerClass = RestController.class;
    private final Class autowiredClass = CustomAutowired.class;

    // 컨트롤러에 서비스를 주입합니다.
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (null != bean.getClass().getAnnotation(controllerClass)) {
            Field[] fieldList = bean.getClass().getDeclaredFields();
            for (Field field : fieldList) {
                if (Objects.nonNull(field.getAnnotation(autowiredClass))) {
                    try {
                        Object beanInstance = CustomBeanRegistry.getInstance().getSingleton(field.getType());

                        // java.lang.reflect.Proxy
                        UserService userService = (UserService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                                new Class<?>[] {UserService.class},
                                new InvocationHandler() {
                                    @Override
                                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                        System.out.println("dynamic proxy");
                                        return method.invoke(beanInstance, args);
                                    }
                                });

                        // cglib.proxy
//                        Enhancer enhancer = new Enhancer();
//                        enhancer.setSuperclass(UserService.class);
//                        enhancer.setCallback(new net.sf.cglib.proxy.InvocationHandler() {
//                            @Override
//                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                                System.out.println("dynamic proxy");
//                                return method.invoke(beanInstance, args);
//                            }
//                        });
//                        UserService userService = (UserService) enhancer.create();



                        final boolean isAccessible = field.isAccessible();

                        field.setAccessible(true);
                        field.set(bean, userService);
                        field.setAccessible(isAccessible);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }

            }
        }
        return bean;

    }

}
