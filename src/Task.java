import java.time.LocalDate;

/**
 * Enhanced Task entity with category support
 * Requirement #7: Categories for entities
 */
public class Task {
    private int id;
    private int workerId;
    private Integer categoryId; // Nullable for tasks without category
    private String title;
    private String description;
    private LocalDate deadline;
    private String status;
    private String priority;

    public Task(int id, int workerId, Integer categoryId, String title, String description, 
                LocalDate deadline, String status, String priority) {
        this.id = id;
        this.workerId = workerId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
    }

    public Task(int workerId, Integer categoryId, String title, String description, 
                LocalDate deadline, String status, String priority) {
        this.workerId = workerId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return id + ": " + title + " (Worker ID: " + workerId + 
               ", Category: " + categoryId + ") Deadline: " + deadline + 
               " Status: " + status + " Priority: " + priority;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getWorkerId() { return workerId; }
    public void setWorkerId(int workerId) { this.workerId = workerId; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}
