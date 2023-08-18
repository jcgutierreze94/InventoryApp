import java.time.LocalDateTime;
import java.util.List;

public class Sale {
    private int id;
    private LocalDateTime timestamp;
    private List<Product> products;

    public Sale(int id, LocalDateTime timestamp, List<Product> products) {
        this.id = id;
        this.timestamp = timestamp;
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", products=" + products +
                '}';
    }

    public int getProductQuantity(Product product) {
        int quantity = 0;
        for (Product p : products) {
            if (p.getId() == product.getId()) {
                quantity++;
            }
        }
        return quantity;
    }

}
