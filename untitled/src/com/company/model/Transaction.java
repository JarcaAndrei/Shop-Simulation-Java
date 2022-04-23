package com.company.model;

import java.util.HashMap;
import java.util.Map;

public class Transaction {

    private final Map<Product, Integer> boughtProducts;
    private Integer total = 0;

    public Transaction() {
        this.boughtProducts = new HashMap<Product, Integer>();
    }

    public Map<Product, Integer> getPurchasedProducts() {
        return boughtProducts;
    }

    public void addToReceipt(Product p, int c) {
        boughtProducts.put(p,c);
    }

    public int displayReceipt() {

        System.out.println("=         Receipt         =");
        for (Map.Entry<Product,Integer> entry: boughtProducts.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            System.out.println(product.getName() + " - quantity: " + quantity + " - price per piece: " + product.getPrice());
            total = total + quantity * product.getPrice();
        }
        System.out.println("----------------------");
        System.out.println("Total cost: " + total +" RON");

        return total;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        total = 0;
        for (Map.Entry<Product,Integer> entry: boughtProducts.entrySet()) {
            total += entry.getValue() * entry.getKey().getPrice();
        }
    }
}