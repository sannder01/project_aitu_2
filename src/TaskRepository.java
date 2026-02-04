import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Task repository with JOIN operations and lambda expressions
 * Requirement #1: JOINs
 * Requirement #3: Lambda expressions
 * SOLID: Single Responsibility - handles only task data access
 * SOLID: Open/Closed - can extend without modifying existing code
 */
public class TaskRepository {

    /**
     * Adds a new task with validation
     * Requirement #6: Data validation
     */
    public void addTask(Task task) throws ValidationException {
        Validator.validateTitle(task.getTitle());
        Validator.validateDeadline(task.getDeadline());
        Validator.validateStatus(task.getStatus());
        Validator.validatePriority(task.getPriority());
        
        String sql = "INSERT INTO tasks(worker_id, category_id, title, description, deadline, status, priority) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, task.getWorkerId());
            if (task.getCategoryId() != null) {
                stmt.setInt(2, task.getCategoryId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, task.getTitle());
            stmt.setString(4, task.getDescription());
            stmt.setDate(5, Date.valueOf(task.getDeadline()));
            stmt.setString(6, task.getStatus());
            stmt.setString(7, task.getPriority());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a task by ID
     */
    public void deleteTask(int id) throws ValidationException {
        Validator.validateId(id);
        
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = Database.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }


    public Optional<TaskDetail> getFullTaskDescription(int taskId) throws ValidationException {
        Validator.validateId(taskId);
        
        String sql = """
            SELECT 
                t.id as task_id,
                t.title as task_title,
                t.description as task_description,
                t.deadline,
                t.status,
                t.priority,
                w.id as worker_id,
                w.name as worker_name,
                w.position as worker_position,
                w.email as worker_email,
                c.id as category_id,
                c.name as category_name,
                c.description as category_description,
                c.color as category_color
            FROM tasks t
            INNER JOIN workers w ON t.worker_id = w.id
            LEFT JOIN categories c ON t.category_id = c.id
            WHERE t.id = ?
        """;
        
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractTaskDetailFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    public List<TaskDetail> getAllTasksWithDetails() {
        List<TaskDetail> taskDetails = new ArrayList<>();
        String sql = """
            SELECT 
                t.id as task_id,
                t.title as task_title,
                t.description as task_description,
                t.deadline,
                t.status,
                t.priority,
                w.id as worker_id,
                w.name as worker_name,
                w.position as worker_position,
                w.email as worker_email,
                c.id as category_id,
                c.name as category_name,
                c.description as category_description,
                c.color as category_color
            FROM tasks t
            INNER JOIN workers w ON t.worker_id = w.id
            LEFT JOIN categories c ON t.category_id = c.id
            ORDER BY t.deadline
        """;
        
        try (Connection conn = Database.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                taskDetails.add(extractTaskDetailFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskDetails;
    }


    public List<TaskDetail> getTasksByCategory(int categoryId) throws ValidationException {
        Validator.validateId(categoryId);
        
        List<TaskDetail> taskDetails = new ArrayList<>();
        String sql = """
            SELECT 
                t.id as task_id,
                t.title as task_title,
                t.description as task_description,
                t.deadline,
                t.status,
                t.priority,
                w.id as worker_id,
                w.name as worker_name,
                w.position as worker_position,
                w.email as worker_email,
                c.id as category_id,
                c.name as category_name,
                c.description as category_description,
                c.color as category_color
            FROM tasks t
            INNER JOIN workers w ON t.worker_id = w.id
            INNER JOIN categories c ON t.category_id = c.id
            WHERE c.id = ?
            ORDER BY t.deadline
        """;
        
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    taskDetails.add(extractTaskDetailFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskDetails;
    }


    public void updateTaskStatus(int taskId, String status) throws ValidationException {
        Validator.validateId(taskId);
        Validator.validateStatus(status);
        
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, taskId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Task> getTasksByStatus(String status) {
        return getAllTasks().stream()
            .filter(task -> task.getStatus().equalsIgnoreCase(status))
            .collect(Collectors.toList());
    }


    public List<Task> getOverdueTasks() {
        LocalDate today = LocalDate.now();
        return getAllTasks().stream()
            .filter(task -> task.getDeadline().isBefore(today))
            .filter(task -> !task.getStatus().equalsIgnoreCase("Completed"))
            .sorted((t1, t2) -> t1.getDeadline().compareTo(t2.getDeadline()))
            .collect(Collectors.toList());
    }


    public List<Task> getTasksByPriority(String priority) {
        return getAllTasks().stream()
            .filter(task -> task.getPriority().equalsIgnoreCase(priority))
            .collect(Collectors.toList());
    }

    // Helper methods
    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        return new Task(
            rs.getInt("id"),
            rs.getInt("worker_id"),
            (Integer) rs.getObject("category_id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getDate("deadline").toLocalDate(),
            rs.getString("status"),
            rs.getString("priority")
        );
    }

    private TaskDetail extractTaskDetailFromResultSet(ResultSet rs) throws SQLException {
        return new TaskDetail(
            rs.getInt("task_id"),
            rs.getString("task_title"),
            rs.getString("task_description"),
            rs.getDate("deadline").toLocalDate(),
            rs.getString("status"),
            rs.getString("priority"),
            rs.getInt("worker_id"),
            rs.getString("worker_name"),
            rs.getString("worker_position"),
            rs.getString("worker_email"),
            (Integer) rs.getObject("category_id"),
            rs.getString("category_name"),
            rs.getString("category_description"),
            rs.getString("category_color")
        );
    }
}
