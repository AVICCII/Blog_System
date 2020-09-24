package com.aviccii.cc;

import java.util.Calendar;

/**
 * @author aviccii 2020/9/24
 * @Discrimination
 */
public class timeTest {
    public static void main(String[] args) {
        Calendar instance = Calendar.getInstance();
        instance.set(2999,11,1);
        long timeInMillis = instance.getTimeInMillis();
        System.out.println(timeInMillis);
    }
}
