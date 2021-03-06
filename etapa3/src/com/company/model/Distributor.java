package com.company.model;

public class Distributor {
    private int id;
    private String name;
    private Category category;

    public Distributor(int id, String name, Category category) {
        this.id=id;
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public int getId() {
        return this.id;
    }


}