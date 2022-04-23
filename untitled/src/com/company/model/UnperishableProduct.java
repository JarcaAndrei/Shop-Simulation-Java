package com.company.model;

public class UnperishableProduct extends Product {
    private Integer warrantyLength;


    public UnperishableProduct(String name, Integer price, Integer warrantyLength) {
        super(name, price);
        this.warrantyLength = warrantyLength;
    }

    public Integer getWarrantyLength() {
        return warrantyLength;
    }

    public void setWarrantyLength(Integer warrantyLength) {
        this.warrantyLength = warrantyLength;
    }

    public void display() {
        super.display();

        System.out.println("Unperishable product with " + warrantyLength + " years warranty");
    }
}