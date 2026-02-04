import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class WorkerRepository {

    public void addWorker(Worker worker) throws ValidationException {
        Validator.validateName(worker.getName());
        Validator.validateEmail(worker.getEmail());
        
        String sql = "INSERT INTO workers(name, position, email) VALUES (?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, worker.getName());
            stmt.setString(2, worker.getPosition());
            stmt.setString(3, worker.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWorker(int id) throws ValidationException {
        Validator.validateId(id);
        
        String sql = "DELETE FROM workers WHERE id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Worker> getAllWorkers() {
        List<Worker> workers = new ArrayList<>();
        String sql = "SELECT * FROM workers ORDER BY name";
        try (Connection conn = Database.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                workers.add(new Worker(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("position"),
                    rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workers;
    }


    public List<Worker> getWorkersByPosition(String position) {
        return getAllWorkers().stream()
            .filter(worker -> worker.getPosition().equalsIgnoreCase(position))
            .collect(Collectors.toList());
    }


    public List<Worker> searchWorkersByName(String namePattern) {
        return getAllWorkers().stream()
            .filter(worker -> worker.getName().toLowerCase()
                .contains(namePattern.toLowerCase()))
            .sorted((w1, w2) -> w1.getName().compareTo(w2.getName()))
            .collect(Collectors.toList());
    }
}
