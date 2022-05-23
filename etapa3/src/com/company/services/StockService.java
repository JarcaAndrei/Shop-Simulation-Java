package com.company.services;

import com.company.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.company.services.Environment.conn;


public class StockService {
    private static StockService instanta = null;
    private final Stock stock = new Stock();
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private final CategoryService categoryService = CategoryService.getInstance();


    // constructor privat
    private StockService() {

    }

    public void makeTransaction(Scanner in) {
        Transaction t = new Transaction();

        display();

        int idProduct, quantity;
        Product p;

        try {
            do {
                System.out.print("Choose item, -1 to exit: ");
                idProduct = in.nextInt();
                if (idProduct == -1) {
                    if (t.getPurchasedProducts() == null)
                        return;

                    int total = t.displayReceipt();
                    System.out.print("Confirm y/n: ");
                    String res = in.next();
                    if(res.equals("y"))
                    {
                        stock.confirmPurchase(t);
                        transactions.add(t);
                        System.out.println("Success");

                        return;
                    }
                    System.out.println("Cancel");
                    return;
                }
                p = categoryService.findProduct(idProduct);
                int remaining = stock.get(idProduct);
                System.out.print("How many " + p.getName() + " to get (max. " + remaining + " pieces)");
                quantity = in.nextInt();
                if (quantity > remaining) {
                    System.out.println("Stock not enough");
                    in.nextLine();
                    return;
                }
                t.addToReceipt(idProduct, quantity);
                System.out.println("Added");
            } while(true);
        } catch (Exception e) {System.out.println(e); e.printStackTrace();};
    }

    public void display() {
        stock.display();
    }
    public void displayHistory() {
        stock.displayHistory();
    }

    public static StockService getInstance() {
        if (instanta == null)
            instanta = new StockService();
        return instanta;
    }

    // CRUD

    // Update:
    public boolean update(int id, String nume) {
        return false;
    }

    // Delete
    public boolean delete(int id) {
        return false;
    }

    public void loadFromDb() {
        //
        stock.clear();

        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("select * from delivery");
            while (res.next()) {
                Date data = res.getDate("data");
                Delivery l = new Delivery(res.getInt("id"),res.getInt("idDistributor"),data);
                //
                String query = String.format("select productID, quantity from productdelivery where deliveryID=%d", res.getInt("id"));
                Statement stmt2 = conn.createStatement();
                ResultSet prodCant = stmt2.executeQuery(query);
                while (prodCant.next()) {
                    l.addProduct(prodCant.getInt("productID"),prodCant.getInt("quantity"));
                }
                stock.addDelivery(l);
            }
            remakeStockPrice();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void remakeStockPrice() {
        System.out.println("size"+" " + transactions.size());
        for (Transaction t: transactions) {
            stock.confirmPurchase(t);
        }
    }

    public void displayStockNull() {
        System.out.println("No stock:");
        for(Category c : categoryService.getCategories()) {
            for(Product p: c.getProducts()) {
                if (!stock.getStock().containsKey(p.getId()) || stock.get(p.getId()) == 0) {
                    System.out.println("[" + p.getId() + "] " + p.getName());
                }
            }

        }
    }

    public void addDelivery(Delivery l) {
        try {
            int id = l.getId();
            String query = "insert into delivery () values(null, ?, ?)";
//            String query = "insert into Livrare values(null, '%s', %d)",  new SimpleDateFormat("yyyy-MM-dd").format(l.getData()), l.getIdDistribuitor());

            PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            st.setString(1,new SimpleDateFormat("yyyy-MM-dd").format(l.getData()));
            st.setInt(2,l.getIdDistributor());
            st.executeUpdate();
            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    l.setId(generatedKeys.getLong(1));
                }

            } catch (Exception e) {e.printStackTrace();}

            for(Map.Entry<Integer,Integer> pc : l.getProductsQuantities().entrySet()) {
                String query2 = String.format("insert into productdelivery values (%d,%d,%d)",l.getId(),pc.getKey(),pc.getValue());
                System.out.println(query2);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query2);
            }
            loadFromDb();
        } catch (Exception e) {
            System.err.println(e);

        }

    }
}