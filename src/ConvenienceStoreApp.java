import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConvenienceStoreApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ProductDAO productDAO = new ProductDAO();
    private static final SaleDAO saleDAO = new SaleDAO();
    private static final Inventory inventory = new Inventory();

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            printMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    registerSale();
                    break;
                case 2:
                    administerInventory();
                    break;
                case 3:
                    displaySales();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }

        System.out.println("Thank you for using the Convenience Store App!");
    }

    private static void printMainMenu() {
        System.out.println("=== Convenience Store Menu ===");
        System.out.println("1. Register Sale");
        System.out.println("2. Administer Inventory");
        System.out.println("3. Display Sales");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return choice;
    }

    private static void registerSale() {
        List<Product> availableProducts = productDAO.getAllProducts();
        List<ProductSaleInfo> selectedProducts = new ArrayList<>();

        while (true) {
            System.out.println("Available Products:");
            for (int i = 0; i < availableProducts.size(); i++) {
                System.out.println((i + 1) + ". " + availableProducts.get(i).getName());
            }

            System.out.print("Enter the product number to add to the sale (0 to finish): ");
            int productNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (productNumber == 0) {
                break;
            }

            if (productNumber > 0 && productNumber <= availableProducts.size()) {
                Product selectedProduct = availableProducts.get(productNumber - 1);

                System.out.print("Enter the quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                selectedProducts.add(new ProductSaleInfo(selectedProduct, quantity));
                System.out.println(selectedProduct.getName() + " added to the sale.");
            } else {
                System.out.println("Invalid product number.");
            }
        }

        if (!selectedProducts.isEmpty()) {
            List<Product> products = new ArrayList<>();
            for (ProductSaleInfo productInfo : selectedProducts) {
                Product product = productInfo.getProduct();
                int quantitySold = productInfo.getQuantity();

                if (product.getQuantity() >= quantitySold) {
                    product.setQuantity(product.getQuantity() - quantitySold);
                    products.add(product);
                    // Update the product quantity in the database using your ProductDAO
                    productDAO.updateProduct(product);
                } else {
                    System.out.println("Not enough quantity in inventory for " + product.getName());
                }
            }

            Sale sale = new Sale(0, LocalDateTime.now(), products);
            saleDAO.insertSale(sale);
            System.out.println("Sale registered successfully!");
        }
    }

    private static void administerInventory() {
        boolean exit = false;

        while (!exit) {
            printInventoryMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    addProductToInventory();
                    break;
                case 2:
                    updateProductInInventory();
                    break;
                case 3:
                    removeProductFromInventory();
                    break;
                case 4:
                    listProductsInInventory();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    private static void printInventoryMenu() {
        System.out.println("=== Inventory Management ===");
        System.out.println("1. Add Product to Inventory");
        System.out.println("2. Update Product in Inventory");
        System.out.println("3. Remove Product from Inventory");
        System.out.println("4. List Products in Inventory");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private static void addProductToInventory() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter product quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Product newProduct = new Product(0, name, price, quantity);
        productDAO.insertProduct(newProduct);
        System.out.println("Product added to inventory!");
    }

    private static void updateProductInInventory() {
        System.out.print("Enter product ID to update: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Product productToUpdate = productDAO.getProductById(productId);

        if (productToUpdate != null) {
            System.out.print("Enter updated product name: ");
            String newName = scanner.nextLine();

            System.out.print("Enter updated product price: ");
            double newPrice = scanner.nextDouble();

            System.out.print("Enter updated product quantity: ");
            int newQuantity = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            productToUpdate.setName(newName);
            productToUpdate.setPrice(newPrice);
            productToUpdate.setQuantity(newQuantity);

            productDAO.updateProduct(productToUpdate);
            System.out.println("Product updated in inventory!");
        } else {
            System.out.println("Product with ID " + productId + " not found.");
        }
    }

    private static void removeProductFromInventory() {
        System.out.print("Enter product ID to remove: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Product productToRemove = productDAO.getProductById(productId);

        if (productToRemove != null) {
            productDAO.deleteProduct(productId);
            System.out.println("Product removed from inventory!");
        } else {
            System.out.println("Product with ID " + productId + " not found.");
        }
    }

    private static void listProductsInInventory() {
        List<Product> products = productDAO.getAllProducts();

        System.out.println("=== Products in Inventory ===");
        for (Product product : products) {
            System.out.println("ID: " + product.getId());
            System.out.println("Name: " + product.getName());
            System.out.println("Price: $" + product.getPrice());
            System.out.println("Quantity: " + product.getQuantity());
            System.out.println();
        }
    }

    private static void displaySales() {
        List<Sale> sales = saleDAO.getAllSales();

        System.out.println("=== Sales History ===");
        for (Sale sale : sales) {
            System.out.println("Sale ID: " + sale.getId());
            System.out.println("Timestamp: " + sale.getTimestamp());
            System.out.println("Products and Quantities:");
            for (Product product : sale.getProducts()) {
                int quantity = sale.getProductQuantity(product);
                System.out.println("- " + product.getName() + " - $" + product.getPrice() + " - Quantity: " + quantity);
            }
            System.out.println();
        }
    }
}
