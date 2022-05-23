package com.company.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private Integer id;
    private String name;
    private final List<Product> products;

    public Category(String name) {
        this.name = name;
        this.products = new ArrayList<Product>();
    }
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
        this.products = new ArrayList<Product>();
    }
    public String getName() {
        return name;
    }

    public void setName(String nume) {
        this.name = nume;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void display() {
        System.out.print(name + ": [ ");
        for (Product p : products) {
            System.out.print("(" + p.getId() + "):" + p.getName() + " " );
        }
        System.out.println(" ]");
    }

    public Product getProduct(int idProduct) {
        return products.stream().filter(p -> p.getId().equals(idProduct)).findFirst().orElse(null);
    }
    public void addProduct(Product p) {
        this.products.add(p);
    }

    public Integer getId() {
        return id;
    }

    public String getNume() {
        return name;
    }

}