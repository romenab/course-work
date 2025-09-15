package app;

import java.util.*;

public class Warehouse {
    private Tasks allProducts;

    public Warehouse(Tasks allProducts) {
        this.allProducts = allProducts;
    }
    public void getTable() { 
        System.out.printf("%-36s %-15s %-8s %-20s %-20s %-6s %-12s%n", "ID", "Name", "Price €", "Created", "Last updated", "Units", "Expiration"); 
        for (Map<String, Object> product : allProducts.getAllProducts()) { 
            double price = ((Number)product.get("price")).doubleValue() / 100.0; 
            System.out.printf("%-36s %-15s %-8.2f %-20s %-20s %-6d %-12s%n", 
                              product.get("id"), 
                              product.get("name"), 
                              price, 
                              product.get("createdAt"), 
                              product.get("updatedAt"), 
                              ((Number)product.get("units")).intValue(), 
                              product.get("expiredAt")); 
        } 
    }

    public void getChanges() {
        System.out.printf("%-10s %-10s %-36s %-15s %-6s %-20s%n",
                "User", "Action", "ID", "Name", "Units", "Last updated");

        for (Map<String, Object> change : allProducts.getChangesMade()) {
            System.out.printf("%-10s %-10s %-36s %-15s %-6d %-20s%n",
                    change.get("username"),
                    change.get("action"),
                    change.get("id"),
                    change.get("name"),
                    ((Number)change.get("units")).intValue(),
                    change.get("updatedAt"));
        }
    }

    public void chooseAction(int userAction, Scanner scanner) {
        switch (userAction) {
            case 1 -> allProducts.add(scanner);
            case 2 -> allProducts.update(scanner);
            case 3 -> allProducts.delete(scanner);
            case 4 -> {
                getChanges();
                System.out.print("Make more changes (1) or exit (2): ");
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 2) {
                    allProducts.save("products.json", "changeLog.json");
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
            }
            case 5 -> allProducts.report();
            case 6 -> {
                allProducts.save("products.json", "changeLog.json");
                System.out.println("Goodbye!");
                System.exit(0);
            }
            default -> System.out.println("Invalid input!");
        }
        getTable();
    }

    public void getMenu() {
        System.out.println("1. Add new product");
        System.out.println("2. Update product");
        System.out.println("3. Delete product");
        System.out.println("4. View changes log");
        System.out.println("5. View report");
        System.out.println("6. Exit");
    }
}
