package com.company.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Delivery {
    private Distributor distributor;
    private Date data;
    private final Map<Product,Integer> productsQuantities;

    public Delivery(Distributor distributor, Date data) {
        this.distributor = distributor;
        this.data = data;
        this.productsQuantities = new HashMap<Product,Integer>();
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void addProduct(Product p, int quantity) {
        productsQuantities.put(p,quantity);
    }

    public Map<Product, Integer> getProductsQuantities() {
        return productsQuantities;
    }

    public void display() {
        int cost = 0;
        System.out.println("Date: " + new SimpleDateFormat("yyyy-MM-dd").format(data));
        System.out.println("Distributor: "+ distributor.getName());
        System.out.println("Delivery containment: ");
        for(Map.Entry<Product,Integer> entry : productsQuantities.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            System.out.println(product.getName() + " - quantity: " + quantity + " - price per piece: " + product.getPrice());
            cost = cost + quantity * product.getPrice();
        }
        System.out.println("----------------------");
        System.out.println("Total price: " + cost +" RON");
        System.out.println();

    }


}