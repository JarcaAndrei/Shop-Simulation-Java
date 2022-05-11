package com.company.services;

import com.company.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Service {
    private final Stock stock = new Stock();
    private final ArrayList<Category> categories = new ArrayList<Category>();
    private final ArrayList<Distributor> distributors = new ArrayList<Distributor>();
    private AuditService audit = AuditService.getInstance();
    private FileWriterService gout = FileWriterService.getInstance();
    // 1
    public void addCategory(Scanner in) {
        String name;
        System.out.println("Enter name of category:");
        name = in.nextLine();
        Category cat = new Category(name);
        categories.add(cat);
        audit.log("category added");
        ArrayList<String> out_message =new ArrayList<String>();
        out_message.add(name);
        gout.write("./date/Category.csv",out_message);
    }
    private void createCategory(String nume) {

        Category cat = new Category(nume);
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

            ArrayList<String> out_message =new ArrayList<String>();
            out_message.add(name);
            out_message.add(String.valueOf(id));
            gout.write("./date/Distributor.csv",out_message);

            audit.log("distributor added");
            System.out.println("Added successfully");

        } catch (Exception e) {
            System.out.println("Wrong input");
        }
        in.nextLine();
    }
    private void createDistributor(String nume, int id) {
        Distributor d = new Distributor(nume,  categories.get(id));
        distributors.add(d);
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
        audit.log("get products from category");

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
            int idCategory = in.nextInt();
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
            int av_warr;
            if (res == 1) {
                System.out.println("Availability of product in months:");
                av_warr = in.nextInt();
                p = new PerishableProduct(name,price,av_warr);
            } else if (res == 2) {
                System.out.println("Warranty length - years:");
                av_warr = in.nextInt();
                p = new UnperishableProduct(name,price,av_warr);
            } else { return; }

            categories.get(idCategory).addProduct(p);
            in.nextLine();

            audit.log("add product");
            ArrayList<String> out_message =new ArrayList<String>();
            out_message.add(String.valueOf(idCategory));
            out_message.add(name);
            out_message.add(String.valueOf(price));
            out_message.add(String.valueOf(res));
            out_message.add(String.valueOf(av_warr));
            gout.write("./date/Product.csv",out_message);



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
                audit.log("add delivery");

                ArrayList<String> out_message =new ArrayList<String>();
                out_message.add(String.valueOf(idDistributor));
                out_message.add(new SimpleDateFormat("yyyy-MM-dd").format(data));
                out_message.add(String.valueOf(l.getProductsQuantities().size()));
                for(Map.Entry<Product,Integer> el : l.getProductsQuantities().entrySet()) {
                    out_message.add(String.valueOf(el.getKey().getId()));
                    out_message.add(String.valueOf(el.getValue()));
                }
                gout.write("./date/Delivery.csv",out_message);
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
            audit.log("product details");
        } catch (Exception e) {System.out.println(e);}
        in.nextLine();

    }
    //6
    public void displayMoneyAndProdStock() {
        stock.display();
        audit.log("display money+stock");
    }

    private Product findProduct(int id) {
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
        audit.log("display delivery history");
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

                    System.out.print("Confirm purchase? y/n:");
                    String res = in.next();
                    if(res.equals("y"))
                    {
                        stock.confirmPurchase(t);
                        System.out.println("Transaction successful");
                        audit.log("client transaction");
                        return;
                    }
                    System.out.println("Transaction canceled");
                    return;
                }
                p = findProduct(idProduct);
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
        audit.log("out of stock products");
    }

    public void readFromCsv() {

        String categoryPATH = "./date/Category.csv";
        String distributorPATH =  "date/Distributor.csv";
        String deliveryPATH =  "date/Delivery.csv";
        String productPATH =  "date/Product.csv";

        FileReaderService fsv = FileReaderService.getInstance();

        // category
        ArrayList<ArrayList<String>> categoryList = fsv.read(categoryPATH);
        for(ArrayList<String> l : categoryList) {
            createCategory(l.get(0));
        }

        // distributor
        ArrayList<ArrayList<String>> distributorList = fsv.read(distributorPATH);
        for(ArrayList<String> l : distributorList) {
            createDistributor(l.get(0),Integer.parseInt(l.get(1)));
        }

        // product

        ArrayList<ArrayList<String>> productList = fsv.read(productPATH);
        for(ArrayList<String> l : productList) {
            Integer idCategory = Integer.parseInt(l.get(0));
            Integer productType = Integer.parseInt(l.get(3));
            Product p;
            if (productType == 1) {
                p = new PerishableProduct(l.get(1),Integer.parseInt(l.get(2)),Integer.parseInt(l.get(4)));
            } else {
                p = new UnperishableProduct(l.get(1),Integer.parseInt(l.get(2)),Integer.parseInt(l.get(4)));
            }
            categories.get(idCategory).addProduct(p);
        }

        // delivery
        ArrayList<ArrayList<String>> deliveryList = fsv.read(deliveryPATH);
        for(ArrayList<String> l : deliveryList) {
            int distributorID = Integer.parseInt(l.get(0));
            Category cat = distributors.get(distributorID).getCategory();

            try {
                Date data = new SimpleDateFormat("yyyy-MM-dd").parse(l.get(1));
                int productNr = Integer.parseInt(l.get(2));
                Delivery liv = new Delivery(distributors.get(distributorID),data);
                for (int i=0; i<productNr*2; i+=2) {
                    int productID = Integer.parseInt(l.get(3+i));
                    int price = Integer.parseInt(l.get(4+i));

                    liv.addProduct(cat.getProduct(productID),price);
                }
                stock.addDelivery(liv);
            }catch (ParseException p) {
                System.out.println("Bad date format");
            }
        }
        System.out.println("loaded data");

    }
}

