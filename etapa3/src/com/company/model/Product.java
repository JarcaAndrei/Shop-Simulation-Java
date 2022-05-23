package com.company.model;

public class Product {

    private Integer id;
    private String name;
    private Integer price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Product(int id,String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void display() {
        System.out.println("= Product details =");
        System.out.println("Name: " + name);
        System.out.println("Price: " + price + " RON");


    }
}