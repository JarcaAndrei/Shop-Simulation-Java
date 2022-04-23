package com.company.services;

import com.company.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Service {
    private final Stock stock = new Stock();
    private final ArrayList<Category> categories = new ArrayList<Category>();
    private final ArrayList<Distributor> distributors = new ArrayList<Distributor>();

    // 1
    public void addCategory(Scanner in) {
        String name;
        System.out.println("Enter name of category:");
        name = in.nextLine();
        Category cat = new Category(name);
        categories.add(cat);

    }
    //3
    public void addDistributor(Scanner in) {

        if (categories.size() == 0) {
            System.out.println("error, no category added");
            return;
        }
        try {
            System.out.println("Enter name of distributor:");
            String name = in.nextLine();
            displayCategory();
            System.out.println("Enter id of category the distributor will provide:");

            int id = in.nextInt();
            Distributor d = new Distributor(name, categories.get(id));
            distributors.add(d);
            System.out.println("Added successfully");

        } catch (Exception e) {
            System.out.println("Wrong input");
        }
        in.nextLine();
    }


    private void displayCategory() {
        System.out.println("=      Categories    =");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println(i + ": " + categories.get(i).getName());
        }
    }


    private void displayDistributors() {
        System.out.println("=   Distributors   =");
        for (int i = 0; i < distributors.size(); i++) {
            Distributor d = distributors.get(i);
            System.out.println(i + ": " + d.getName() + " -- " + d.getCategory().getName());

        }
    }

    //4
    public Category getProdFromCategory(Scanner in) {
        if (categories.isEmpty())
        {
            System.out.println("error, no category added");
            return null;
        }
        displayCategory();
        System.out.println();
        Category c = null;
        System.out.println("Enter category id for which to get products:");
        try {
            int res = in.nextInt();
            c = categories.get(res);
            c.display();
        } catch (Exception e) {
            System.out.println("Wrong input");
        }
        in.nextLine();
        return c;
    }
    //2
    public void addProduct(Scanner in) {
        String name;
        Integer price;
        if (categories.isEmpty())
        {
            System.out.println("error, no category added");
            return;
        }
        displayCategory();
        try {
            System.out.println("Enter the category you want the product to belong to:");
            int idCategorie = in.nextInt();
            in.nextLine();
            System.out.println("Enter product name:");
            name = in.nextLine();

            System.out.println("Enter product price:");
            price = in.nextInt();

            System.out.println("Type of product");
            System.out.println("1. Perishable");
            System.out.println("2. Unperishable");
            int res = in.nextInt();

            Product p;
            if (res == 1) {
                System.out.println("Availability of product in months:");
                int availability = in.nextInt();
                p = new PerishableProduct(name,price,availability);
            } else if (res == 2) {
                System.out.println("Warranty length - years:");
                int warranty = in.nextInt();
                p = new UnperishableProduct(name,price,warranty);
            } else { return; }

            categories.get(idCategorie).addProduct(p);
            in.nextLine();


        } catch (Exception e) {
            System.out.println("Wrong input");
        }
    }
    //5
    public void purchaseProdFromDistributor(Scanner in) throws ParseException {
        if (distributors.size() == 0 ) {
            System.out.println("error, no distributor added");
            return;
        }

        Date data;
        displayDistributors();
        System.out.print("Enter distributor id:");
        int idDistributor = in.nextInt();
        System.out.print("Delivery date (yyyy-mm-dd): ");
        String dataStr = in.next();
        System.out.println(dataStr);
        data = new SimpleDateFormat("yyyy-MM-dd").parse(dataStr);
        Distributor d = distributors.get(idDistributor);
        Delivery l = new Delivery(d,data);

        System.out.println("Products that " + d.getName() +" distributor can deliver: ");
        Category cat = d.getCategory();
        cat.display();

        int idProduct;
        int quantity;
        do {
            try {
                System.out.print("Enter product id or press -1 to continue:");
                idProduct = in.nextInt();
                if (idProduct == -1)
                    break;
                Product p = cat.getProduct(idProduct);
                System.out.print("How many " + p.getName() + " products to deliver:");
                quantity = in.nextInt();
                l.addProduct(p,quantity);
                System.out.println("Added successfully");

            } catch(Exception e) {
                System.out.print("Wrong input");

            }
        } while (true);

        stock.addDelivery(l);

        System.out.println("Delivery successfully placed");

        in.nextLine();


    }
    //9
    public void productDetails(Scanner in) {
        if (categories.isEmpty())
        {
            System.out.println("error, no category added");
            return;
        }
        Category catSelected = getProdFromCategory(in);
        int idProduct;
        Product p;
        System.out.print("Enter product id:");

        try {
            idProduct = in.nextInt();
            p = catSelected.getProduct(idProduct);
            p.display();
        } catch (Exception e) {System.out.println(e);}
        in.nextLine();

    }
    //6
    public void displayMoneyAndProdStock() {
        stock.display();
    }

    private Product findProdus(int id) {
        for (Category cat : categories) {
            Product p = cat.getProduct(id);
            if (p != null)
                return p;
        }
        return null;
    }
    //10
    public void deliveryHistory() {
        stock.displayHistory();
    }
    //7
    public void clientPurchase(Scanner in) {
        Transaction t = new Transaction();

        displayMoneyAndProdStock();

        int idProduct, quantity;
        Product p;

        try {
            do {
                System.out.print("Enter product id or -1 to continue:");
                idProduct = in.nextInt();
                if (idProduct == -1) {
                    if (t.getPurchasedProducts() == null)
                        return;

                    int total = t.displayReceipt();
                    System.out.print("Confirm purchase? y/n:");
                    String res = in.next();
                    if(res.equals("y"))
                    {
                        stock.confirmPurchase(t);
                        System.out.println("Transaction successful");
                        return;
                    }
                    System.out.println("Transaction canceled");
                    return;
                }
                p = findProdus(idProduct);
                int remaining = stock.get(p);
                System.out.print("How many pieces of " + p.getName() + " to get? (max. " + remaining + " pieces)");
                quantity = in.nextInt();
                if (quantity > remaining) {
                    System.out.println("Insufficient funds");
                    in.nextLine();
                    return;
                }
                t.addToReceipt(p, quantity);
                System.out.println("Added successfully");
            } while(true);
        } catch (Exception e) {System.out.println(e);};
        in.nextLine();


    }
    //8
    public void outOfStock() {
        System.out.println("Products out of stock:");
        for(Category c : categories) {
            for(Product p: c.getProducts()) {
                if (!stock.getStock().containsKey(p) || stock.get(p) == 0) {
                    System.out.println("[" + p.getId() + "] " + p.getName());
                }
            }

        }
    }

}