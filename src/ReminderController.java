import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class ReminderController {
    private final WorkerRepository workerRepo = new WorkerRepository();
    private final TaskRepository taskRepo = new TaskRepository();
    private final CategoryRepository categoryRepo = new CategoryRepository();
    private final UserRepository userRepo = new UserRepository();
    private final SecurityService securityService = new SecurityService();
    
    private User currentUser; // Current logged-in user

    // ========== Authentication ==========
    

    public boolean login(String username, String password) {
        Optional<User> user = userRepo.authenticate(username, password);
        if (user.isPresent()) {
            currentUser = user.get();
            System.out.println("Welcome, " + currentUser.getUsername() + 
                             " (Role: " + currentUser.getRole() + ")");
            return true;
        }
        System.out.println("Invalid username or password");
        return false;
    }

    public void logout() {
        currentUser = null;
        System.out.println("Logged out successfully");
    }

    public User getCurrentUser() {
        return currentUser;
    }


    

    public void addWorker(String name, String position, String email) {
        try {
            securityService.requireAddWorkerPermission(currentUser);
            workerRepo.addWorker(new Worker(name, position, email));
            System.out.println("Worker added successfully");
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("VALIDATION ERROR: " + e.getMessage());
        }
    }

    public void deleteWorker(int id) {
        try {
            securityService.requireDeleteWorkerPermission(currentUser);
            workerRepo.deleteWorker(id);
            System.out.println("Worker deleted successfully");
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("VALIDATION ERROR: " + e.getMessage());
        }
    }


    public void showWorkers() {
        try {
            securityService.requireAuthenticated(currentUser);
            List<Worker> workers = workerRepo.getAllWorkers();
            if (workers.isEmpty()) {
                System.out.println("No workers found");
            } else {
                workers.forEach(System.out::println);
            }
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    public void searchWorkersByPosition(String position) {
        try {
            securityService.requireAuthenticated(currentUser);
            List<Worker> workers = workerRepo.getWorkersByPosition(position);
            if (workers.isEmpty()) {
                System.out.println("No workers found with position: " + position);
            } else {
                workers.forEach(System.out::println);
            }
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    public void addTask(int workerId, Integer categoryId, String title, 
                       String description, LocalDate deadline, String priority) {
        try {
            securityService.requireTaskModificationPermission(currentUser);
            taskRepo.addTask(new Task(workerId, categoryId, title, description, 
                                     deadline, "Pending", priority));
            System.out.println("Task added successfully");
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("VALIDATION ERROR: " + e.getMessage());
        }
    }


    public void deleteTask(int id) {
        try {
            securityService.requireTaskModificationPermission(currentUser);
            taskRepo.deleteTask(id);
            System.out.println("Task deleted successfully");
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("VALIDATION ERROR: " + e.getMessage());
        }
    }


    public void updateTaskStatus(int taskId, String status) {
        try {
            securityService.requireTaskModificationPermission(currentUser);
            taskRepo.updateTaskStatus(taskId, status);
            System.out.println("Task status updated successfully");
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("VALIDATION ERROR: " + e.getMessage());
        }
    }


    public void showTasks() {
        try {
            securityService.requireAuthenticated(currentUser);
            List<Task> tasks = taskRepo.getAllTasks();
            if (tasks.isEmpty()) {
                System.out.println("No tasks found");
            } else {
                tasks.forEach(System.out::println);
            }
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    public void showFullTaskDescription(int taskId) {
        try {
            securityService.requireAuthenticated(currentUser);
            Optional<TaskDetail> taskDetail = taskRepo.getFullTaskDescription(taskId);
            if (taskDetail.isPresent()) {
                System.out.println("\n" + taskDetail.get());
            } else {
                System.out.println("Task not found with ID: " + taskId);
            }
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("VALIDATION ERROR: " + e.getMessage());
        }
    }


    public void showAllTasksWithDetails() {
        try {
            securityService.requireAuthenticated(currentUser);
            List<TaskDetail> taskDetails = taskRepo.getAllTasksWithDetails();
            if (taskDetails.isEmpty()) {
                System.out.println("No tasks found");
            } else {
                taskDetails.forEach(td -> System.out.println("\n" + td));
            }
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    public void showTasksByStatus(String status) {
        try {
            securityService.requireAuthenticated(currentUser);
            List<Task> tasks = taskRepo.getTasksByStatus(status);
            if (tasks.isEmpty()) {
                System.out.println("No tasks found with status: " + status);
            } else {
                tasks.forEach(System.out::println);
            }
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    public void showOverdueTasks() {
        try {
            securityService.requireAuthenticated(currentUser);
            List<Task> tasks = taskRepo.getOverdueTasks();
            if (tasks.isEmpty()) {
                System.out.println("No overdue tasks");
            } else {
                System.out.println("=== OVERDUE TASKS ===");
                tasks.forEach(System.out::println);
            }
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    public void showTasksByPriority(String priority) {
        try {
            securityService.requireAuthenticated(currentUser);
            List<Task> tasks = taskRepo.getTasksByPriority(priority);
            if (tasks.isEmpty()) {
                System.out.println("No tasks found with priority: " + priority);
            } else {
                tasks.forEach(System.out::println);
            }
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    public void addCategory(String name, String description, String color) {
        try {
            securityService.requireTaskModificationPermission(currentUser);
            categoryRepo.addCategory(new Category(name, description, color));
            System.out.println("Category added successfully");
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("VALIDATION ERROR: " + e.getMessage());
        }
    }


    public void deleteCategory(int id) {
        try {
            securityService.requireManagerOrHigher(currentUser);
            categoryRepo.deleteCategory(id);
            System.out.println("Category deleted successfully");
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("VALIDATION ERROR: " + e.getMessage());
        }
    }


    public void showCategories() {
        try {
            securityService.requireAuthenticated(currentUser);
            List<Category> categories = categoryRepo.getAllCategories();
            if (categories.isEmpty()) {
                System.out.println("No categories found");
            } else {
                categories.forEach(System.out::println);
            }
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    public void showTasksByCategory(int categoryId) {
        try {
            securityService.requireAuthenticated(currentUser);
            List<TaskDetail> tasks = taskRepo.getTasksByCategory(categoryId);
            if (tasks.isEmpty()) {
                System.out.println("No tasks found for this category");
            } else {
                tasks.forEach(td -> System.out.println("\n" + td));
            }
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("VALIDATION ERROR: " + e.getMessage());
        }
    }


    public void addUser(String username, String password, String email, UserRole role) {
        try {
            securityService.requireAdmin(currentUser);
            userRepo.addUser(new User(username, password, email, role));
            System.out.println("User added successfully");
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("VALIDATION ERROR: " + e.getMessage());
        }
    }


    public void showUsers() {
        try {
            securityService.requireAdmin(currentUser);
            List<User> users = userRepo.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("No users found");
            } else {
                users.forEach(System.out::println);
            }
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void updateUserRole(int userId, UserRole newRole) {
        try {
            securityService.requireAdmin(currentUser);
            userRepo.updateUserRole(userId, newRole);
            System.out.println("User role updated successfully");
        } catch (AuthorizationException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("VALIDATION ERROR: " + e.getMessage());
        }
    }
}
