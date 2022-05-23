package com.company.model;

import com.company.services.CategoryService;

import java.util.*;

public class Stock {
    private final Set<Delivery> deliveryHistory;
    private final Map<Integer,Integer> stock;
    static private Integer moneyStock = 0;
    private CategoryService categoryService = CategoryService.getInstance();


    Comparator<Delivery> sortByDate = new Comparator<Delivery>() {
        @Override
        public int compare(Delivery l1, Delivery l2) {
            return l1.getData().compareTo(l2.getData());
        }
    };
    public Stock() {

        this.stock = new HashMap<Integer, Integer>();
        this.deliveryHistory = new TreeSet<Delivery>(sortByDate);
    }

    public Map<Integer, Integer> getStock() {
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

        stock.forEach((idProduct, quantity) -> {
            if(quantity > 0) {
                Product product = categoryService.findProduct(idProduct);
                System.out.println("[" + product.getId() + "] " + product.getName() + ": " + quantity);
            }
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

    public int get(int p) {
        return stock.get(p);
    }

    public void confirmPurchase(Transaction t) {
        moneyStock += t.getTotal();

        for (Map.Entry<Integer, Integer> entry : t.getPurchasedProducts().entrySet()) {
            Integer p = entry.getKey();
            Integer c = entry.getValue();
            stock.put(entry.getKey(), stock.get(p) - c);
        }
    }

    public void clear() {
        deliveryHistory.clear();
        stock.clear();
        moneyStock = 0;
    }
}