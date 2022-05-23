package com.company.services;

import com.company.model.Category;
import com.company.model.Product;
import com.company.model.UnperishableProduct;
import com.company.model.PerishableProduct;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import static com.company.services.Environment.conn;


public class ProductService {
    private static ProductService instanta = null;

    // constructor privat
    private ProductService() {


    }

    public static ProductService getInstance() {
        if (instanta == null)
            instanta = new ProductService();
        return instanta;
    }

    public void createProduct(Category cat, Scanner in) {
        try {
            in.nextLine();
            System.out.println("Product name:");
            String name = in.nextLine();

            System.out.println("Product price:");
            Integer price = in.nextInt();

            System.out.println("Type of product:");
            System.out.println("1. Perishable");
            System.out.println("2. Unperishable");
            int res = in.nextInt();

            int val;
            if (res == 1) {
                System.out.println("Availability: (in months)");
                val = in.nextInt();
                insertDb(name, price, cat.getId(), "perishable", null, val);
            } else if (res == 2) {
                System.out.println("Warranty: (in years)");
                val = in.nextInt();
                insertDb(name, price, cat.getId(), "unperishable", val, null);
            }
        } catch (Exception e) {
            System.err.println("Wrong input");
        }
    }

    public Product getProdus(int id) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(String.format("select * from product where id=%d", id));
            String name = res.getString("name");
            Integer price =res.getInt("price");
            String productType = res.getString("productType");
            Integer warranty = productType.equals("unperishable") ? res.getInt("warranty") : null;
            Integer availability = productType.equals("perishable") ? res.getInt("availability") : null;


            if (productType == "unperishable")
                return new UnperishableProduct(id,name,price,warranty);
            return new PerishableProduct(id,name,price,availability);
        } catch (Exception e) {
            System.err.println("BD");
        }
        return null;
    }
    // CRUD DB

    // Create
    public boolean insertDb(String name, Integer price, Integer idCategory, String productType, Integer warranty, Integer availability) {

        try {
            String query = String.format("insert into product values (null,'%s','%d','%d','%s',%d,%d);", name, price, idCategory, productType, warranty, availability);
            System.out.println(query);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("Inserted product!");
            return true;

        } catch (Exception e) {
            System.err.println("error insert db!");
            return false;
        }
    }
    // Update:
    public boolean update(int id, String name) {
        return false;
    }

    // Delete
    public boolean delete(int id) {
        return false;
    }

    public void loadFromDb() {
    }
}