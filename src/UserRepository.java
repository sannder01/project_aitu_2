import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User repository for authentication and authorization
 * Requirement #5: Role Management
 * SOLID: Single Responsibility - handles only user data access
 */
public class UserRepository {

    public void addUser(User user) throws ValidationException {
        Validator.validateName(user.getUsername());
        Validator.validateEmail(user.getEmail());
        
        String sql = "INSERT INTO users(username, password, email, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword()); // In production, hash the password!
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole().name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) throws ValidationException {
        Validator.validateId(id);
        
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username";
        try (Connection conn = Database.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    UserRole.valueOf(rs.getString("role"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Authenticates a user by username and password
     * In production, use hashed passwords!
     */
    public Optional<User> authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // In production, hash and compare!
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        UserRole.valueOf(rs.getString("role"))
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Updates user role (Admin only operation)
     */
    public void updateUserRole(int userId, UserRole newRole) throws ValidationException {
        Validator.validateId(userId);
        
        String sql = "UPDATE users SET role = ? WHERE id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newRole.name());
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
