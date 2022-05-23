package com.company.services;

import com.company.model.Category;
import com.company.model.Product;
import com.company.model.UnperishableProduct;
import com.company.model.PerishableProduct;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import static com.company.services.Environment.*;


public class CategoryService {
    private static CategoryService instance = null;

    private final ArrayList<Category> categories = new ArrayList<Category>();

    // constructor privat
    private CategoryService() {

    }

    // afiseaza lista categorii
    public Category selectCategory(String mesaj, Scanner in) {
        System.out.println("=      Categories    =");
        for (Category c : categories) {
            System.out.println(c.getId() + ": " + c.getNume());
        }
        System.out.println(mesaj);
        try {
            return getCategory(in.nextInt());
        } catch (Exception e) {
            System.out.println("wrong input");
            return null;
        }
    }

    public Category getCategory(int id) {
        return categories.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    // creaza categorie noua
    public void createCategory(String nume) {

        insertDb(nume);
        loadFromDb();

    }

    // load din db in ram
    private void loadCategory(int id, String nume) {

        Category cat = new Category(id, nume);
        categories.add(cat);

    }

    public static CategoryService getInstance() {
        if (instance == null)
            instance = new CategoryService();
        return instance;
    }

    // CRUD DB

    // Create
    public boolean insertDb(String name) {
        try {
            String query = String.format("insert into category values (null,'%s');", name);
            Statement stmt = conn.createStatement();

            stmt.executeUpdate(query);

            System.out.println("Success");
            return true;

        } catch (Exception e) {
            System.err.println("Error");
            return false;
        }
    }

    public Product findProduct(int id) {
        for (Category cat : categories) {
            Product p = cat.getProduct(id);
            if (p != null)
                return p;
        }
        return null;
    }

    // Read: toata lista
    public void loadFromDb() {
        // incarc categoriile
        categories.clear();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("select * from category");
            while (res.next())
                loadCategory(res.getInt("id"), res.getString("name"));

            //incarc produsele categoriilor din db
            stmt = conn.createStatement();
            res = stmt.executeQuery("select * from product");
            while (res.next()) {
                Product p;
                int id = res.getInt("id");
                String nume = res.getString("name");
                int pret = res.getInt("price");
                String tip = res.getString("productType");

                if (tip.equals("unperishable")){
                    int val = res.getInt("warranty");
                    p = new UnperishableProduct(id,nume,pret,val);
                } else {
                    int val = res.getInt("availability");
                    p = new PerishableProduct(id,nume,pret,val);
                }
                // adauga in ram
                addProductToCategory(p, res.getInt("categoryID"));
            }

        } catch (Exception e) {
        }
    }

    private void addProductToCategory(Product p, int idCategory) {
        getCategory(idCategory).addProduct(p);
    }


    public int getSize() {
        return categories.size();
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }
}