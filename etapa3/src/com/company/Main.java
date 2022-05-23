package com.company;

import com.company.services.Service;

import java.util.Scanner;

public class Main {

    public static void main(String[] args)  {
        Scanner in = new Scanner(System.in);
        Service service = new Service();
        service.loadData(in);

        System.out.println("-Shop simulation-");
        while (true) {
            System.out.println("Enter the command: ");
            System.out.println("1. Category add");
            System.out.println("2. Product add");
            System.out.println("3. Distributor add");
            System.out.println("4. Get products from a category");
            System.out.println("5. Purchase products from a distributor");
            System.out.println("6. Display current money and product stock");
            System.out.println("7. Sell products to client");
            System.out.println("8. Display products with stock 0");
            System.out.println("9. Product details");
            System.out.println("10. Distributor purchase history");
            System.out.println("11. Update distributor name");
            System.out.println("12. Delete a category");
            System.out.println("13. Exit");
            String command = in.nextLine();
            try {
                switch (command) {
                    case "1" -> service.addCategory(in);
                    case "2" -> service.addProduct(in);
                    case "3" -> service.addDistributor(in);
                    case "4" -> service.getProdFromCategory(in);
                    case "5" -> service.purchaseProdFromDistributor(in);
                    case "6" -> service.displayMoneyAndProdStock();
                    case "7" -> service.clientPurchase(in);
                    case "8" -> service.outOfStock();
                    case "9" -> service.productDetails(in);
                    case "10" -> service.deliveryHistory();
                    case "11" -> service.changeDistributorName(in);
                    case "12" -> service.deleteCategory(in);
                    case "13" -> {
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}