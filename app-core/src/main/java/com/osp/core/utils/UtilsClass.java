package com.osp.core.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UtilsClass {

    public static <T> List<Field> getAllFields(T t) {
        List<Field> fields = new ArrayList<>();
        Class clazz = t.getClass();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static <T> Field[] getDeclaredFields(T t) {
        return t.getClass().getDeclaredFields();
    }

    public static <T> Field[] getFields(T t) {
        return t.getClass().getFields();
    }

}
