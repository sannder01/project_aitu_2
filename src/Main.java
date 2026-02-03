import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Main application with comprehensive menu
 * Demonstrates all 7 requirements
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ReminderController controller = new ReminderController();
        
        // Login required
        if (!loginMenu(sc, controller)) {
            System.out.println("Exiting...");
            return;
        }
        
        boolean running = true;

        while (running) {
            printMainMenu();
            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> workerMenu(sc, controller);
                    case 2 -> taskMenu(sc, controller);
                    case 3 -> categoryMenu(sc, controller);
                    case 4 -> userMenu(sc, controller);
                    case 5 -> reportMenu(sc, controller);
                    case 9 -> {
                        controller.logout();
                        if (!loginMenu(sc, controller)) {
                            running = false;
                        }
                    }
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                sc.nextLine(); // Clear buffer
            }
        }

        sc.close();
        System.out.println("Exiting...");
    }

    private static boolean loginMenu(Scanner sc, ReminderController controller) {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        return controller.login(username, password);
    }

    private static void printMainMenu() {
        System.out.println("\n========== TASK REMINDER SYSTEM ==========");
        System.out.println("1. Worker Management");
        System.out.println("2. Task Management");
        System.out.println("3. Category Management");
        System.out.println("4. User Management (Admin)");
        System.out.println("5. Reports & Analytics");
        System.out.println("9. Logout & Switch User");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static void workerMenu(Scanner sc, ReminderController controller) {
        System.out.println("\n=== WORKER MANAGEMENT ===");
        System.out.println("1. Show All Workers");
        System.out.println("2. Add Worker");
        System.out.println("3. Delete Worker");
        System.out.println("4. Search Workers by Position");
        System.out.print("Choose an option: ");
        
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> controller.showWorkers();
            case 2 -> {
                System.out.print("Name: ");
                String name = sc.nextLine();
                System.out.print("Position: ");
                String pos = sc.nextLine();
                System.out.print("Email: ");
                String email = sc.nextLine();
                controller.addWorker(name, pos, email);
            }
            case 3 -> {
                System.out.print("Worker ID to delete: ");
                int id = sc.nextInt();
                controller.deleteWorker(id);
            }
            case 4 -> {
                System.out.print("Position: ");
                String position = sc.nextLine();
                controller.searchWorkersByPosition(position);
            }
            default -> System.out.println("Invalid option!");
        }
    }

    private static void taskMenu(Scanner sc, ReminderController controller) {
        System.out.println("\n=== TASK MANAGEMENT ===");
        System.out.println("1. Show All Tasks");
        System.out.println("2. Show All Tasks with Full Details (JOIN)");
        System.out.println("3. Show Full Task Description (JOIN)");
        System.out.println("4. Add Task");
        System.out.println("5. Delete Task");
        System.out.println("6. Update Task Status");
        System.out.println("7. Show Tasks by Status");
        System.out.println("8. Show Tasks by Priority");
        System.out.println("9. Show Overdue Tasks");
        System.out.println("10. Show Tasks by Category");
        System.out.print("Choose an option: ");
        
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> controller.showTasks();
            case 2 -> controller.showAllTasksWithDetails();
            case 3 -> {
                System.out.print("Task ID: ");
                int taskId = sc.nextInt();
                controller.showFullTaskDescription(taskId);
            }
            case 4 -> {
                System.out.print("Worker ID: ");
                int wid = sc.nextInt();
                sc.nextLine();
                System.out.print("Category ID (or 0 for none): ");
                int catId = sc.nextInt();
                sc.nextLine();
                Integer categoryId = catId > 0 ? catId : null;
                System.out.print("Title: ");
                String title = sc.nextLine();
                System.out.print("Description: ");
                String desc = sc.nextLine();
                System.out.print("Deadline (YYYY-MM-DD): ");
                String dl = sc.nextLine();
                System.out.print("Priority (Low/Medium/High/Critical): ");
                String priority = sc.nextLine();
                
                try {
                    controller.addTask(wid, categoryId, title, desc, 
                                     LocalDate.parse(dl), priority);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format!");
                }
            }
            case 5 -> {
                System.out.print("Task ID to delete: ");
                int tid = sc.nextInt();
                controller.deleteTask(tid);
            }
            case 6 -> {
                System.out.print("Task ID: ");
                int tid = sc.nextInt();
                sc.nextLine();
                System.out.print("New Status (Pending/In Progress/Completed/Cancelled): ");
                String status = sc.nextLine();
                controller.updateTaskStatus(tid, status);
            }
            case 7 -> {
                System.out.print("Status (Pending/In Progress/Completed/Cancelled): ");
                String status = sc.nextLine();
                controller.showTasksByStatus(status);
            }
            case 8 -> {
                System.out.print("Priority (Low/Medium/High/Critical): ");
                String priority = sc.nextLine();
                controller.showTasksByPriority(priority);
            }
            case 9 -> controller.showOverdueTasks();
            case 10 -> {
                System.out.print("Category ID: ");
                int catId = sc.nextInt();
                controller.showTasksByCategory(catId);
            }
            default -> System.out.println("Invalid option!");
        }
    }

    private static void categoryMenu(Scanner sc, ReminderController controller) {
        System.out.println("\n=== CATEGORY MANAGEMENT ===");
        System.out.println("1. Show All Categories");
        System.out.println("2. Add Category");
        System.out.println("3. Delete Category");
        System.out.print("Choose an option: ");
        
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> controller.showCategories();
            case 2 -> {
                System.out.print("Name: ");
                String name = sc.nextLine();
                System.out.print("Description: ");
                String desc = sc.nextLine();
                System.out.print("Color (e.g., #FF5733): ");
                String color = sc.nextLine();
                controller.addCategory(name, desc, color);
            }
            case 3 -> {
                System.out.print("Category ID to delete: ");
                int id = sc.nextInt();
                controller.deleteCategory(id);
            }
            default -> System.out.println("Invalid option!");
        }
    }

    private static void userMenu(Scanner sc, ReminderController controller) {
        System.out.println("\n=== USER MANAGEMENT (Admin Only) ===");
        System.out.println("1. Show All Users");
        System.out.println("2. Add User");
        System.out.println("3. Update User Role");
        System.out.print("Choose an option: ");
        
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> controller.showUsers();
            case 2 -> {
                System.out.print("Username: ");
                String username = sc.nextLine();
                System.out.print("Password: ");
                String password = sc.nextLine();
                System.out.print("Email: ");
                String email = sc.nextLine();
                System.out.print("Role (ADMIN/MANAGER/EDITOR/VIEWER): ");
                String roleStr = sc.nextLine();
                try {
                    UserRole role = UserRole.valueOf(roleStr.toUpperCase());
                    controller.addUser(username, password, email, role);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid role!");
                }
            }
            case 3 -> {
                System.out.print("User ID: ");
                int userId = sc.nextInt();
                sc.nextLine();
                System.out.print("New Role (ADMIN/MANAGER/EDITOR/VIEWER): ");
                String roleStr = sc.nextLine();
                try {
                    UserRole role = UserRole.valueOf(roleStr.toUpperCase());
                    controller.updateUserRole(userId, role);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid role!");
                }
            }
            default -> System.out.println("Invalid option!");
        }
    }

    private static void reportMenu(Scanner sc, ReminderController controller) {
        System.out.println("\n=== REPORTS & ANALYTICS ===");
        System.out.println("1. Show Overdue Tasks");
        System.out.println("2. Show High Priority Tasks");
        System.out.println("3. Show Pending Tasks");
        System.out.println("4. Show Completed Tasks");
        System.out.print("Choose an option: ");
        
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> controller.showOverdueTasks();
            case 2 -> controller.showTasksByPriority("High");
            case 3 -> controller.showTasksByStatus("Pending");
            case 4 -> controller.showTasksByStatus("Completed");
            default -> System.out.println("Invalid option!");
        }
    }
}
