import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Product> products;

    public Inventory() {
        products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public boolean containsProduct(Product product) {
        return products.contains(product);
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }
}