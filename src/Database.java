import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection manager implementing Singleton pattern
 * SOLID Principle: Single Responsibility - manages only database connections
 */
public class Database {
    private static Database instance;
    private static final String URL = "jdbc:postgresql://localhost:5432/project_aitu";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Smartdrv3";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Private constructor for Singleton pattern
    private Database() {}

    /**
     * Singleton pattern implementation
     * @return single instance of Database
     */
    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
