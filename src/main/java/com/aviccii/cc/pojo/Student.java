package com.aviccii.cc.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author aviccii 2020/9/15
 * @Discrimination
 */
@Data
public class Student implements Serializable {
    public Student(String name, String sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    private String name;
    private String sex;
    private int age;
}
