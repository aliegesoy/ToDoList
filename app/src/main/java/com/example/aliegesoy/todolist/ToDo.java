package com.example.aliegesoy.todolist;

/**
 * Created by Ali on 26.3.2018.
 */

public class ToDo {

    private String name;
    private int flag;

    public ToDo(String name) {
        this.name = name;
        this.flag = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}