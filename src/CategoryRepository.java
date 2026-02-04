import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CategoryRepository {

    public void addCategory(Category category) throws ValidationException {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new ValidationException("Category name cannot be empty");
        }
        
        String sql = "INSERT INTO categories(name, description, color) VALUES (?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setString(3, category.getColor());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategory(int id) throws ValidationException {
        Validator.validateId(id);
        
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name";
        try (Connection conn = Database.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(new Category(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("color")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public Category getCategoryById(int id) throws ValidationException {
        Validator.validateId(id);
        
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("color")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
