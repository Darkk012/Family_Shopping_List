package com.example.family_shopping_list;

public class Family {
    private String name;
    private String password;

    public Family(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Family() {

    }

    public void setName(String userName) {
        this.name = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
