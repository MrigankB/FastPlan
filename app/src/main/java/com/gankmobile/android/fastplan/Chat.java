package com.gankmobile.android.fastplan;

/**
 * Created by Mrigank on 7/14/15.
 */
public class Chat
{
    private String author;
    private String message;

    public Chat(String author, String message)
    {
        this.author = author;
        this.message = message;
    }

    public String getMessage() {return message;}

    public String getAuthor() {return author;}
}
