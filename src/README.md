# Task Reminder System - Enhanced Version

## Quick Start

### Prerequisites
- Java 11 or higher
- PostgreSQL 12 or higher
- PostgreSQL JDBC Driver

### Setup Steps

1. **Create Database**
```bash
psql -U postgres
CREATE DATABASE reminder_db;
\q
```

2. **Initialize Database**
```bash
psql -U postgres -d reminder_db -f init_database.sql
```

3. **Update Database Credentials**
Edit `Database.java` if needed:
```java
private static final String USER = "postgres";
private static final String PASSWORD = "12345678";
```

4. **Compile**
```bash
javac *.java
```

5. **Run**
```bash
java Main
```

### Default Login Credentials

| Username | Password | Role | Permissions |
|----------|----------|------|-------------|
| admin | 12345678 | ADMIN | Full access |
| manager1 | 12345678 | MANAGER | Manage workers & tasks |
| editor1 | 12345678 | EDITOR | Edit tasks |
| viewer1 | 12345678 | VIEWER | View only |

## Features Implemented

### ✅ Requirement 1: JOINs
- `getFullTaskDescription(taskId)` - Combines tasks, workers, and categories
- `getAllTasksWithDetails()` - All tasks with complete information
- `getTasksByCategory()` - Category-filtered tasks with JOIN

### ✅ Requirement 2: Design Patterns
- **Singleton Pattern**: Database connection manager
- **Repository Pattern**: Data access layer separation

### ✅ Requirement 3: Lambda Expressions
- `getTasksByStatus()` - Filter by status
- `getOverdueTasks()` - Find and sort overdue tasks
- `searchWorkersByName()` - Search and sort workers
- `forEach(System.out::println)` - Method references

### ✅ Requirement 4: SOLID Principles
- **S**: Single Responsibility - Each class has one purpose
- **O**: Open/Closed - Extensible without modification
- **L**: Liskov Substitution - Substitutable implementations
- **I**: Interface Segregation - Specific interfaces
- **D**: Dependency Inversion - Depends on abstractions

### ✅ Requirement 5: Role Management
- ADMIN: Full system access
- MANAGER: Manage workers and tasks
- EDITOR: Modify tasks
- VIEWER: Read-only access

### ✅ Requirement 6: Data Validation
- Email format validation
- Name length and character validation
- Date validation (no past deadlines)
- Status and priority validation
- ID validation

### ✅ Requirement 7: Categories
- Task categorization system
- Color-coded categories
- Category-based filtering with JOINs

## Project Structure

```
.
├── Database.java              - Singleton database manager
├── Main.java                  - Application entry point
├── ReminderController.java    - Main controller with secured methods
│
├── Entities/
│   ├── Task.java             - Task entity
│   ├── Worker.java           - Worker entity
│   ├── Category.java         - Category entity
│   ├── User.java             - User entity
│   └── UserRole.java         - User role enum
│
├── DTOs/
│   └── TaskDetail.java       - DTO for JOIN results
│
├── Repositories/
│   ├── TaskRepository.java    - Task data access (with JOINs & lambdas)
│   ├── WorkerRepository.java  - Worker data access
│   ├── CategoryRepository.java - Category data access
│   └── UserRepository.java    - User data access
│
├── Services/
│   ├── SecurityService.java   - Role-based authorization
│   └── Validator.java        - Data validation
│
├── Exceptions/
│   ├── ValidationException.java
│   └── AuthorizationException.java
│
└── init_database.sql         - Database initialization script
```

## Menu Structure

```
MAIN MENU
├── 1. Worker Management
│   ├── Show All Workers
│   ├── Add Worker (Manager+)
│   ├── Delete Worker (Admin)
│   └── Search by Position
│
├── 2. Task Management
│   ├── Show All Tasks
│   ├── Show Tasks with Details (JOIN)
│   ├── Show Full Task Description (JOIN)
│   ├── Add Task (Editor+)
│   ├── Delete Task (Editor+)
│   ├── Update Status (Editor+)
│   ├── Filter by Status
│   ├── Filter by Priority
│   ├── Show Overdue Tasks
│   └── Filter by Category
│
├── 3. Category Management
│   ├── Show All Categories
│   ├── Add Category (Editor+)
│   └── Delete Category (Manager+)
│
├── 4. User Management (Admin)
│   ├── Show All Users
│   ├── Add User
│   └── Update User Role
│
└── 5. Reports & Analytics
    ├── Overdue Tasks
    ├── High Priority Tasks
    ├── Pending Tasks
    └── Completed Tasks
```

## Code Examples

### JOIN Operation
```java
// Get full task description with worker and category data
Optional<TaskDetail> detail = taskRepo.getFullTaskDescription(1);
```

### Lambda Expression
```java
// Find overdue tasks
List<Task> overdue = getAllTasks().stream()
    .filter(task -> task.getDeadline().isBefore(LocalDate.now()))
    .filter(task -> !task.getStatus().equalsIgnoreCase("Completed"))
    .sorted((t1, t2) -> t1.getDeadline().compareTo(t2.getDeadline()))
    .collect(Collectors.toList());
```

### Role-Based Security
```java
// Secured endpoint - only managers and admins can add workers
public void addWorker(String name, String position, String email) {
    securityService.requireManagerOrHigher(currentUser);
    workerRepo.addWorker(new Worker(name, position, email));
}
```

### Data Validation
```java
// Validate before adding
Validator.validateEmail(email);
Validator.validateName(name);
Validator.validateDeadline(deadline);
```

## Testing Checklist

- [ ] Login with different roles
- [ ] Test role-based restrictions
- [ ] View tasks with JOIN data
- [ ] Add/edit/delete operations
- [ ] Test validation errors
- [ ] Filter tasks using lambdas
- [ ] Assign categories to tasks
- [ ] View tasks by category

## For More Details

See `IMPLEMENTATION_GUIDE.md` for comprehensive documentation on:
- Detailed explanation of each requirement
- SOLID principles analysis
- Database schema
- Complete API reference
- Testing procedures

## Notes

- Passwords are stored in plain text for demonstration only
- In production, use bcrypt or similar for password hashing
- Add connection pooling for better performance
- Consider adding logging framework
- Add unit tests for business logic

## Support

For questions or issues, refer to the implementation guide or review the inline code documentation.
