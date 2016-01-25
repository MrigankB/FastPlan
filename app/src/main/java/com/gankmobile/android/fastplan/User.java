package com.gankmobile.android.fastplan;

/**
 * Created by Mrigank on 7/3/2015.
 */
public class User
{
    private String name;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String time;

    public User(String name, String time)
    {
        this.name = name;
        this.time = time;
    }
}
