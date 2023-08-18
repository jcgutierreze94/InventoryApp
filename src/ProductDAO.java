import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public void insertProduct(Product product) {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)")) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(Product product) {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE products SET name = ?, price = ?, quantity = ? WHERE id = ?")) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setInt(4, product.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int productId) {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM products WHERE id = ?")) {
            statement.setInt(1, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, name, price, quantity FROM products");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                Product product = new Product(id, name, price, quantity);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product getProductById(int productId) {
        Product product = null;
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, name, price, quantity FROM products WHERE id = ?")) {
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                product = new Product(id, name, price, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

}
