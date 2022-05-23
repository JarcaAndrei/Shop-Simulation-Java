package com.company.services;

import com.company.model.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import static com.company.services.Environment.*;

public class Service {
    private AuditService audit = AuditService.getInstance();
    private FileWriterService fws = FileWriterService.getInstance();

    private CategoryService categoryService = CategoryService.getInstance();
    private DistributorService distributorService = DistributorService.getInstance();
    private ProductService productService = ProductService.getInstance();
    private StockService stockService = StockService.getInstance();
    // 1
    public void addCategory(Scanner in) {
        String name;
        System.out.println("Enter name of category:");
        name = in.nextLine();

        categoryService.createCategory(name);
        audit.log("category added");
    }

    //3
    public void addDistributor(Scanner in) {

        if (categoryService.getSize() == 0) {
            System.out.println("error, no category added");
            return;
        }
        try {
            System.out.println("Enter name of distributor:");
            String name = in.nextLine();

            Category cat = categoryService.selectCategory("Enter id of category the distributor will provide:",in);
            distributorService.createDistributor(name, cat);

            audit.log("distributor added");
            System.out.println("Added successfully");

        } catch (Exception e) {
            System.out.println("Wrong input");
        }
        in.nextLine();
    }

    //4
    public Category getProdFromCategory(Scanner in) {
        if (categoryService.getSize()==0)
        {
            System.out.println("error, no category added");
            return null;
        }
        Category c = categoryService.selectCategory("Enter category id for which to get products:\n",in);
        c.display();

        in.nextLine();
        audit.log("get products from category");

        return c;
    }
    //2
    public void addProduct(Scanner in) {
        Category cat;

        if (categoryService.getSize() == 0)
        {
            System.out.println("error, no category added");
            return;
        }
        cat = categoryService.selectCategory("Enter the category you want the product to belong to:\n",in);
        productService.createProduct(cat,in);
        categoryService.loadFromDb();
        in.nextLine();
        audit.log("add product");

    }
    //5
    public void purchaseProdFromDistributor(Scanner in) throws ParseException {
        if (distributorService.getSize() == 0 ) {
            System.out.println("error, no distributor added");
            return;
        }
        distributorService.loadFromDb();
        categoryService.loadFromDb();
        stockService.loadFromDb();

        distributorService.displayDistributors();

        System.out.print("Enter distributor id:");
        int idDistributor = in.nextInt();
        if (idDistributor == -1) {
            System.out.println("cancel...");
            return;
        }

        Distributor d = distributorService.getDistribuitor(idDistributor);
        if (d == null)
            return;
        System.out.print("Delivery date (yyyy-mm-dd): ");
        String dataStr = in.next();
        System.out.println(dataStr);
        Date data = new SimpleDateFormat("yyyy-MM-dd").parse(dataStr);
        Delivery l = new Delivery(-1,idDistributor,data);

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
                l.addProduct(idProduct,quantity);
                System.out.println("Added successfully");
                audit.log("add delivery");



            } catch(Exception e) {
                System.out.print("Wrong input");

            }
        } while (true);
        if (l.getProductsQuantities().isEmpty()) {
            System.out.println("nothing bought, so won't add anything");
            in.nextLine();
            return;

        }
        stockService.addDelivery(l);

        System.out.println("Delivery successfully placed");

        in.nextLine();


    }
    //9
    public void productDetails(Scanner in) {
        if (categoryService.getSize() == 0)
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
        stockService.display();
        audit.log("display money+stock");
    }
    //10
    public void deliveryHistory() {
        stockService.displayHistory();
        audit.log("display delivery history");
    }
    //7
    public void clientPurchase(Scanner in) {
        stockService.makeTransaction(in);
        audit.log("make transaction");
        in.nextLine();
    }
    //8
    public void outOfStock() {
        stockService.displayStockNull();
        audit.log("out of stock products");
    }
    public void loadData(Scanner in) {

        // load
        categoryService.loadFromDb();
        distributorService.loadFromDb();
        stockService.loadFromDb();
    }
    public void changeDistributorName(Scanner in) {

        Distributor d = distributorService.displayDistributors("Choose distributor id to change:",in);
        in.nextLine();
        System.out.println("new name:");
        String name = in.nextLine();
        try {
            // CRUD Db Update
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("update distributor set `name`='%s' where `id`=%d",name,d.getId()));
            distributorService.loadFromDb();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteCategory(Scanner in) {
        Category c = categoryService.selectCategory("Introdu id-ul categoriei pe care doresti sa o stergi:(sau -1 pt anulare)",in);
        if (c != null) {
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(String.format("delete from category where id=%d", c.getId()));
                loadData(in);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

