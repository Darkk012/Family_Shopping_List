package com.example.family_shopping_list;

public class Family {
    private String name;
    private String password;
    private Integer listSize=0;

    public Integer getListSize() {
        return listSize;
    }

    public void setListSize(Integer listSize) {
        this.listSize = listSize;
    }

    public Family(String name, String password) {
        this.name = name;
        this.password = password;
        this.listSize=0;
    }

    public Family() {
        this.listSize=0;
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
