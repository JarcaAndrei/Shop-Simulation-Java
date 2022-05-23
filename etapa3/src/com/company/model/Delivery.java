package com.company.model;

import com.company.services.CategoryService;
import com.company.services.DistributorService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class Delivery {
    private int id;
    private int idDistributor;
    private Date data;
    private Map<Integer,Integer> productsQuantities;
    private CategoryService categoryService = CategoryService.getInstance();
    private DistributorService distributorService = DistributorService.getInstance();

    public Delivery(int id, int idDistributor, Date data) {
        this.id = id;
        this.idDistributor = idDistributor;
        this.data = data;
        this.productsQuantities = new HashMap<Integer,Integer>();
    }

    public int getIdDistributor() {
        return idDistributor;
    }
    public Distributor getDistributor() {
        return distributorService.getDistribuitor(this.idDistributor);
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void addProduct(int idProduct, int quantity) {
        productsQuantities.put(idProduct,quantity);
    }

    public Map<Integer, Integer> getProductsQuantities() {
        return productsQuantities;
    }

    public void display() {
        int cost = 0;
        System.out.println("Date: " + new SimpleDateFormat("yyyy-MM-dd").format(data));
        System.out.println("Distributor: "+ getDistributor().getName());
        System.out.println("Delivery containment: ");
        for(Map.Entry<Integer,Integer> entry : productsQuantities.entrySet()) {
            Product product = categoryService.findProduct(entry.getKey());
            int quantity = entry.getValue();
            System.out.println(product.getName() + " - quantity: " + quantity + " - price per piece: " + product.getPrice());
            cost = cost + quantity * product.getPrice();
        }
        System.out.println("----------------------");
        System.out.println("Total price: " + cost +" RON");
        System.out.println();

    }


    public int getId() {
        return id;
    }
    public void setId(long id)
    {
        this.id=(int)id;
    }
}