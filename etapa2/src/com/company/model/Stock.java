package com.company.model;

import java.util.*;

public class Stock {
    private final Set<Delivery> deliveryHistory;
    private final Map<Product,Integer> stock;
    static private Integer moneyStock = 0;


    Comparator<Delivery> sortByDate = new Comparator<Delivery>() {
        @Override
        public int compare(Delivery l1, Delivery l2) {
            return l1.getData().compareTo(l2.getData());
        }
    };
    public Stock() {

        this.stock = new HashMap<Product, Integer>();
        this.deliveryHistory = new TreeSet<Delivery>(sortByDate);
    }

    public Map<Product, Integer> getStock() {
        return stock;
    }

    public void addDelivery(Delivery l) {
        deliveryHistory.add(l);
        l.getProductsQuantities().forEach((product, quantity) -> {
            int old = stock.getOrDefault(product, 0);
            stock.put(product, old+quantity);
        });
    }

    public void display() {
        System.out.println("=     Products in stock     =");

        stock.forEach((product, quantity) -> {
            if(quantity > 0)
                System.out.println("[" + product.getId() + "] " + product.getName() + ": " + quantity);
        });

        System.out.println("---------------------------");
        System.out.println("Funds amount: " + moneyStock + " RON");

    }

    public void displayHistory() {

        if (deliveryHistory.isEmpty())
        {
            System.out.println("No purchases");
            return;
        }
        System.out.println("=     Purchase history     =");

        for (Delivery l : deliveryHistory) {
            l.display();

        }
    }

    public int get(Product p) {
        return stock.get(p);
    }

    public void confirmPurchase(Transaction t) {
        moneyStock += t.getTotal();

        for (Map.Entry<Product, Integer> entry : t.getPurchasedProducts().entrySet()) {
            Product p = entry.getKey();
            Integer c = entry.getValue();
            stock.put(p, stock.get(p) - c);
        }
    }

}