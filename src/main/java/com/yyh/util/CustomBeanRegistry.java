package com.yyh.util;

import org.springframework.beans.factory.BeanCreationNotAllowedException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CustomBeanRegistry {

    private final Map<Class, Object> singletonObjects = new ConcurrentHashMap<>(256);

    private static CustomBeanRegistry instance;
    public synchronized static CustomBeanRegistry getInstance() {
        if (instance == null) {
            instance = new CustomBeanRegistry();
        }
        return instance;
    }

    public void registerSingleton(Class<?> beanInstance)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Class[] con = {};
        singletonObjects.put(beanInstance, beanInstance.getDeclaredConstructor(con).newInstance());
    }

    public Object getSingleton(Class<?> classType) {

        Set<Class> keys = singletonObjects.keySet();
        for (Class key : keys) {
            if (key == classType) {
                return singletonObjects.get(key);

            }

            Class superClass = key.getSuperclass();
            if (superClass == classType) {
                return singletonObjects.get(key);

            }

            Class[] instanceList = key.getInterfaces();
            for (Class instanceType : instanceList) {
                if (instanceType == classType) {
                    return singletonObjects.get(key);

                }
            }
        }

        throw new BeanCreationNotAllowedException(classType.getName(), "not found bean");

    }

}
