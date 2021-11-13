package com.psi.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class T {
    /**
     * 泛型擦除
     *
     * @param args
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Integer> list = new ArrayList<>();
        Class<? extends List> al = list.getClass();
        Method add = al.getDeclaredMethod("add", Object.class);
        add.setAccessible(true);
        add.invoke(list, "test");
        add.invoke(list, "reflect");
        for (Object o : list) {
            System.out.println(o);
        }
    }
}
