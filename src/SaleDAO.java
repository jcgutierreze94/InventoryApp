import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {
    public void insertSale(Sale sale) {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO sales (timestamp) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setTimestamp(1, java.sql.Timestamp.valueOf(sale.getTimestamp()));
            statement.executeUpdate();

            int saleId;
            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    saleId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating sale failed, no ID obtained.");
                }
            }

            try (PreparedStatement saleProductsStatement = connection.prepareStatement(
                    "INSERT INTO sale_products (sale_id, product_id, quantity) VALUES (?, ?, ?)")) {
                for (Product product : sale.getProducts()) {
                    saleProductsStatement.setInt(1, saleId);
                    saleProductsStatement.setInt(2, product.getId());
                    saleProductsStatement.setInt(3, 1); // Assuming one quantity per product in a sale
                    saleProductsStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, timestamp FROM sales");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                LocalDateTime timestamp = resultSet.getTimestamp("timestamp").toLocalDateTime();
                List<Product> products = getProductsForSale(connection, id);
                Sale sale = new Sale(id, timestamp, products);
                sales.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sales;
    }

    private List<Product> getProductsForSale(Connection connection, int saleId) throws SQLException {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT p.id, p.name, p.price, p.quantity FROM products p " +
                        "JOIN sale_products sp ON p.id = sp.product_id " +
                        "WHERE sp.sale_id = ?")) {
            statement.setInt(1, saleId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                Product product = new Product(id, name, price, quantity);
                products.add(product);
            }
        }
        return products;
    }

    public double calculateTotalSales(LocalDateTime startDate, LocalDateTime endDate) {
        double totalSales = 0;
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT SUM(p.price) AS total_price FROM products p " +
                             "JOIN sale_products sp ON p.id = sp.product_id " +
                             "JOIN sales s ON sp.sale_id = s.id " +
                             "WHERE s.timestamp >= ? AND s.timestamp <= ?")) {
            statement.setTimestamp(1, java.sql.Timestamp.valueOf(startDate));
            statement.setTimestamp(2, java.sql.Timestamp.valueOf(endDate));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalSales = resultSet.getDouble("total_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalSales;
    }
}
