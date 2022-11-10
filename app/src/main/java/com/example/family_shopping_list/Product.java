package com.example.family_shopping_list;

public class Product {
    private String name;
    private Integer number;
    private String information;
    private Integer state=1;

    /*
    state:
    1: csak a listában van
    2: Kosárban van
    3: megvan véve
    */

    public Product(String name, Integer number, String information) {
        this.name = name;
        this.number = number;
        this.information = information;
    }

    public Product(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Override
    public String toString() {
        String message= "Termék: "+name+" \n Mennyiség: "+number;
        if(!(information == null)) message+="\n Egyéb információ: "+information;
        return message;
    }
}
