import java.util.Scanner;
import app.*;

public class index {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        User user = new User();
        user.logIn(scanner);

        Tasks allProducts = new Tasks(user);
        allProducts.load("products.json", "changeLog.json");

        Warehouse show = new Warehouse(allProducts);

        System.out.println("Welcome to Warehouse!");
        show.getTable();

        while (true) {
            show.getMenu();
            System.out.print("Enter your action: ");
            int userAction = Integer.parseInt(scanner.nextLine());
            show.chooseAction(userAction, scanner);
        }
    }
}
