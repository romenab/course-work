package app;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import java.time.LocalDateTime;
import java.util.UUID;


public class Tasks {
    private List<Map<String, Object>> allProducts;
    private List<Map<String, Object>> changesMade;
    private User user;

    public Tasks(User user) {
        this.user = user;
        this.allProducts = new ArrayList<>();
        this.changesMade = new ArrayList<>();
    }

    public void add(Scanner scanner) {
        while (true) {
            System.out.print("Enter your product name: ");
            String name = scanner.nextLine();
            if (name.isEmpty()) {
                System.out.println("Invalid input!");
                continue;
            }
        
            System.out.print("Enter your unit quantity: ");
            int units = Integer.parseInt(scanner.nextLine());
            if (units < 0) {
                System.out.println("Invalid input!");
                continue;
            }

            System.out.print("Enter your product price (€): ");
            double price = Double.parseDouble(scanner.nextLine());
            if (price < 0) {
                System.out.println("Invalid input!");
                continue;
            }

            System.out.print("Enter your expiration date (yyyy-MM-dd): ");
            String exp = scanner.nextLine();
            String expiredAt = exp.isEmpty() ? null : exp;

        String now = LocalDateTime.now().toString();

        Map<String, Object> product = new LinkedHashMap<>();
        product.put("id", UUID.randomUUID().toString());
        product.put("name", name);
        product.put("price", (int)(price * 100));
        product.put("createdAt", now);
        product.put("updatedAt", now);
        product.put("units", units);
        product.put("expiredAt", expiredAt);

        allProducts.add(product);
        System.out.println("Product added successfully!");
        changesLog("Added", (String)product.get("id"), name, units);  
        break;

        }      
    }

    public void update(Scanner scanner) {
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter new unit quantity: ");
        int newUnits = Integer.parseInt(scanner.nextLine());

        for (Map<String, Object> product : allProducts) {
            if (product.get("id").equals(id)) {
                product.put("units", newUnits);
                product.put("updatedAt", LocalDateTime.now().toString());
                System.out.println("Product updated successfully!");
                changesLog("Updated", id, (String)product.get("name"), newUnits);
                return;
            }
        }
        System.out.println("Product not found!");
    }

    public void delete(Scanner scanner) {
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();

        Iterator<Map<String, Object>> iterator = allProducts.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> product = iterator.next();
            if (product.get("id").equals(id)) {
                iterator.remove();
                System.out.println("Product deleted successfully!");
                changesLog("Deleted", id, (String)product.get("name"), (int)product.get("units"));
                return;
            }
        }
        System.out.println("Product not found!");
    }

    private void changesLog(String action, String id, String name, int units) {
        Map<String, Object> change = new LinkedHashMap<>();
        change.put("username", user.getUsername());
        change.put("action", action);
        change.put("id", id);
        change.put("name", name);
        change.put("units", units);
        change.put("updatedAt", LocalDateTime.now().toString());
        changesMade.add(change);
    }

    public void report() {
        int totalUnits = 0;
        int totalValue = 0;

        for (Map<String, Object> product : allProducts) {
            totalUnits += (int)product.get("units");
            totalValue += (int)product.get("price");
            
        }

        System.out.println("****************************");
        System.out.println("Warehouse products in total: " + totalUnits);
        System.out.println("Total value of all products: " + (totalValue / 100.0) + " €");
        System.out.println("****************************");
    }

    public void save(String productsFile, String changesFile) {
        try (FileWriter prodWriter = new FileWriter(productsFile);
             FileWriter changeWriter = new FileWriter(changesFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(allProducts, prodWriter);
            gson.toJson(changesMade, changeWriter);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data", e);
        }
    }

    public void load(String productsFile, String changesFile) {
        try {
            Gson gson = new Gson();

            try (FileReader reader = new FileReader(productsFile)) {
                allProducts = gson.fromJson(reader, List.class);
            }
            try (FileReader reader = new FileReader(changesFile)) {
                changesMade = gson.fromJson(reader, List.class);
            }
        } catch (Exception e) {
            System.out.println("No data loaded yet.");
            allProducts = new ArrayList<>();
            changesMade = new ArrayList<>();
        }
    }

    public List<Map<String, Object>> getAllProducts() {
        return allProducts;
    }

    public List<Map<String, Object>> getChangesMade() {
        return changesMade;
    }
}
