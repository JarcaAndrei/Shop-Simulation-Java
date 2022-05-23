package com.company.model;

import com.company.services.CategoryService;

import java.util.HashMap;
import java.util.Map;

public class Transaction {

    private final Map<Integer, Integer> boughtProducts;
    private Integer total = 0;
    private CategoryService categoryService = CategoryService.getInstance();

    public Transaction() {
        this.boughtProducts = new HashMap<Integer, Integer>();
    }

    public Map<Integer, Integer> getPurchasedProducts() {
        return boughtProducts;
    }

    public void addToReceipt(int idP, int c) {
        boughtProducts.put(idP,c);
    }

    public int displayReceipt() {

        System.out.println("=         Receipt         =");
        for (Map.Entry<Integer,Integer> entry: boughtProducts.entrySet()) {
            Product product = categoryService.findProduct(entry.getKey());
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
        for (Map.Entry<Integer,Integer> entry: boughtProducts.entrySet()) {
            total += entry.getValue() * categoryService.findProduct(entry.getKey()).getPrice();
        }
    }
}