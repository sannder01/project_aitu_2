import java.time.LocalDate;


public class TaskDetail {
    // Task fields
    private int taskId;
    private String taskTitle;
    private String taskDescription;
    private LocalDate deadline;
    private String status;
    private String priority;
    
    // Worker fields (from JOIN)
    private int workerId;
    private String workerName;
    private String workerPosition;
    private String workerEmail;
    
    // Category fields (from JOIN)
    private Integer categoryId;
    private String categoryName;
    private String categoryDescription;
    private String categoryColor;

    public TaskDetail(int taskId, String taskTitle, String taskDescription, 
                      LocalDate deadline, String status, String priority,
                      int workerId, String workerName, String workerPosition, String workerEmail,
                      Integer categoryId, String categoryName, String categoryDescription, String categoryColor) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
        this.workerId = workerId;
        this.workerName = workerName;
        this.workerPosition = workerPosition;
        this.workerEmail = workerEmail;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryColor = categoryColor;
    }

    @Override
    public String toString() {
        return String.format(
            "Task #%d: %s\n" +
            "  Description: %s\n" +
            "  Status: %s | Priority: %s | Deadline: %s\n" +
            "  Worker: %s (%s) - %s\n" +
            "  Category: %s - %s [%s]",
            taskId, taskTitle, taskDescription, status, priority, deadline,
            workerName, workerPosition, workerEmail,
            categoryName != null ? categoryName : "N/A",
            categoryDescription != null ? categoryDescription : "",
            categoryColor != null ? categoryColor : ""
        );
    }

    // Getters
    public int getTaskId() { return taskId; }
    public String getTaskTitle() { return taskTitle; }
    public String getTaskDescription() { return taskDescription; }
    public LocalDate getDeadline() { return deadline; }
    public String getStatus() { return status; }
    public String getPriority() { return priority; }
    public int getWorkerId() { return workerId; }
    public String getWorkerName() { return workerName; }
    public String getWorkerPosition() { return workerPosition; }
    public String getWorkerEmail() { return workerEmail; }
    public Integer getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getCategoryDescription() { return categoryDescription; }
    public String getCategoryColor() { return categoryColor; }
}
