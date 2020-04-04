package com.example.privatevanmanagement.ChatModule.Model;

/**
 * Created by vaibhav on 1/15/18.
 */

public class ChatList {

    public ChatList() {

    }

    public ChatList(String name, String id) {
        this.name = name;
        this.id = id;
    }

    String name;

    String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
