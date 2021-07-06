package com.yyh;

import com.yyh.dto.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Test {
    public void aa() throws ClassNotFoundException {
        // 클래스에서 클래스 타입 조회
        Class class1 = User.class;

        // 클래스 이름에서 클래스타입 조회
        Class class2 = Class.forName("클래스이름");

        // 메소드 가져오기
        Method[] m = class1.getMethods();

        // 변수 가져오기
        Field[] f = class1.getFields();

        // 생성자 가져오기
        Constructor[] cs = class1.getConstructors();

        // 인터페이스
        Class[] inter = class1.getInterfaces();

        // 부모 클래스
        Class superClass = class1.getSuperclass();
    }
}
