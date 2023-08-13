import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InventoryManager {

    private static Map<String, Integer> inventory = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("---- Convenience Store Inventory Management ----");
            System.out.println("1. Add Item to Inventory");
            System.out.println("2. Update Item Quantity");
            System.out.println("3. View Current Inventory");
            System.out.println("4. Remove Item from Inventory");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addItemToInventory(scanner);
                    break;
                case 2:
                    updateItemQuantity(scanner);
                    break;
                case 3:
                    viewCurrentInventory();
                    break;
                case 4:
                    removeItemFromInventory(scanner);
                    break;
                case 5:
                    System.out.println("Exiting Inventory Manager. Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private static void addItemToInventory(Scanner scanner) {
        System.out.println("---- Add Item to Inventory ----");
        System.out.print("Enter the item name: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter the quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        if (inventory.containsKey(itemName)) {
            int currentQuantity = inventory.get(itemName);
            inventory.put(itemName, currentQuantity + quantity);
        } else {
            inventory.put(itemName, quantity);
        }

        System.out.println(itemName + " added to the inventory.");
    }

    private static void updateItemQuantity(Scanner scanner) {
        System.out.println("---- Update Item Quantity ----");
        System.out.print("Enter the item name: ");
        String itemName = scanner.nextLine();

        if (!inventory.containsKey(itemName)) {
            System.out.println("Item not found in the inventory.");
            return;
        }

        System.out.print("Enter the new quantity: ");
        int newQuantity = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        inventory.put(itemName, newQuantity);
        System.out.println(itemName + " quantity updated to " + newQuantity);
    }

    private static void viewCurrentInventory() {
        System.out.println("---- Current Inventory ----");
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    private static void removeItemFromInventory(Scanner scanner) {
        System.out.println("---- Remove Item from Inventory ----");
        System.out.print("Enter the item name: ");
        String itemName = scanner.nextLine();

        if (!inventory.containsKey(itemName)) {
            System.out.println("Item not found in the inventory.");
            return;
        }

        inventory.remove(itemName);
        System.out.println(itemName + " removed from the inventory.");
    }
}