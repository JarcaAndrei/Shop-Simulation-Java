package com.company.model;

public class PerishableProduct extends Product {
    private Integer availability;

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public PerishableProduct(int id, String name, Integer price, Integer availability) {
        super(id,name, price);
        this.availability = availability;
    }

    public void display() {
        super.display();

        System.out.println("Product is perishable with: " + availability + " months as availability");
    }
}