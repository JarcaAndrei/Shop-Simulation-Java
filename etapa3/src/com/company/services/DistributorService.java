package com.company.services;

import com.company.model.Category;
import com.company.model.Distributor;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import static com.company.services.Environment.conn;


public class DistributorService {
    private static DistributorService instanta = null;
    private final ArrayList<Distributor> distribuitori = new ArrayList<Distributor>();
    private CategoryService categoryService = CategoryService.getInstance();

    // constructor privat
    private DistributorService() {

    }
    public Distributor getDistribuitor(int id) {
        return distribuitori.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
    }

    public static DistributorService getInstance() {
        if (instanta == null)
            instanta = new DistributorService();
        return instanta;
    }

    public void createDistributor(String nume, Category cat) {
        insertDb(nume,cat.getId());
        loadFromDb();
    }

    // load din db in ram
    private void loadDistributor(int id, String nume, int idCat) {

        Distributor d = new Distributor(id, nume, categoryService.getCategory(idCat));
        distribuitori.add(d);

    }
    public int getSize() {
        return distribuitori.size();
    }

    public void displayDistributors() {
        System.out.println("=   Distributors   =");
        for (Distributor d:distribuitori) {
            System.out.println(d.getId() + ": " + d.getName() + " -- " + d.getCategory().getNume());

        }
    }
    public Distributor displayDistributors(String mesaj, Scanner in) {
        System.out.println("=   Distributors   =");
        for (Distributor d:distribuitori)
            System.out.println(d.getId() + ": " + d.getName() + " -- " + d.getCategory().getNume());

        System.out.println(mesaj);
        try {
            return getDistribuitor(in.nextInt());
        } catch (Exception e) {
            System.out.println("wrong name");
            return null;
        }
    }
    // CRUD db

    // Create
    public boolean insertDb(String nume, int idCat) {
        try {
            String query = String.format("insert into distributor values (null, '%s', '%d');",nume, idCat);
            Statement stmt = conn.createStatement();
            System.out.println(query);

            stmt.executeUpdate(query);

            System.out.println("Succes!");
            return true;

        } catch (Exception e) {
            System.err.println("Eroare insert db!");
            return false;
        }
    }

    // Read: toata lista
    public Boolean loadFromDb() {
        distribuitori.clear();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("select * from distributor");
            while (res.next()) {
                this.loadDistributor(res.getInt("id"), res.getString("name"), res.getInt("categoryID"));
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }




}