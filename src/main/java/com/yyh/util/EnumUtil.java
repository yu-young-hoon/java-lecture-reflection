package com.yyh.util;

import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnumUtil {

    private static final String ENUM_BASE_METHOD_NAME = "name";

    public static <M extends Map<K, R>, K, R extends Enum> R enumOf(M map, K key) {
        map.computeIfAbsent(key, k -> {
            throw new RuntimeException(key + " 키가 없어요");
        });
        return map.get(key);
    }

    public static <M extends Map<K, R>, K, R extends Enum> Optional<R> optionalEnumOf(M map, K key) {
        return Optional.ofNullable(map.get(key));
    }

    public static <E extends Enum> Map<String, E> createEnumMap(Class<E> type) {
        return createEnumMap(type, null, String.class);
    }

    public static <E extends Enum> Map<String, E> createEnumMap(Class<E> type, String fieldName) {
        return createEnumMap(type, fieldName, String.class);
    }

    public static <R, E extends Enum> Map<R, E> createEnumMap(Class<E> type, String fieldName, Class<R> returnClass) {

        final String methodName = Strings.isNullOrEmpty(fieldName) ? ENUM_BASE_METHOD_NAME : "get" + capitalize(fieldName);
        E[] enumValues = getEnumValues(type);

        Map<R, E> map = new HashMap<>();
        for (E e : enumValues) {
            R key = extractEnumFieldValue(methodName, e, returnClass);
            if(key == null) continue;
            map.computeIfPresent(key, (k,v) -> {
                throw new RuntimeException("데이터가 유일하지 않습니다. key:" + k + ", value:" + v);
            });

            map.put(key, e);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Enum> E[] getEnumValues(Class<E> type) {
        E[] enumValues;
        try {

            Field field = type.getDeclaredField("$VALUES");
            field.setAccessible(true);
            Object o = field.get(null);
            enumValues = (E[]) o;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("enum에 열거형 데이터가 없습니다.", e);
        }
        return enumValues;
    }

    private static <R, E extends Enum> R extractEnumFieldValue(String methodName, E enumValue, Class<R> returnClass) {
        try {
            if(ENUM_BASE_METHOD_NAME.equals(methodName)){
                return returnClass.cast(enumValue.name());
            }
            @SuppressWarnings("unchecked")
            Object invoke = enumValue.getDeclaringClass().getDeclaredMethod(methodName, (Class<?>[]) null).invoke(enumValue);
            return returnClass.cast(invoke);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignore) {
            //ignore exception
        }
        return null;
    }

    public static <X extends RuntimeException> void exceptionIf(boolean check, Supplier<? extends X> exception) {
        if (check) {
            throw exception.get();
        }
    }

    public static String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}