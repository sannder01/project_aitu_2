# Task Reminder System - Implementation Documentation

## Overview
This enhanced version of the Task Reminder System implements all 7 required features:
1. JOINs
2. Design Patterns (Singleton, Factory concepts)
3. Lambda Expressions
4. SOLID Principles
5. Role Management (Secured Endpoints)
6. Data Validation
7. Categories for Entities

---

## Requirement #1: Implementation of JOINs

### Example: GetFullTaskDescription(taskId)
Located in `TaskRepository.java`:

```java
public Optional<TaskDetail> getFullTaskDescription(int taskId)
```

This method demonstrates a complex JOIN operation that combines data from three tables:
- **tasks** (main table)
- **workers** (INNER JOIN - every task must have a worker)
- **categories** (LEFT JOIN - tasks may or may not have a category)

**SQL Query:**
```sql
SELECT 
    t.id, t.title, t.description, t.deadline, t.status, t.priority,
    w.id, w.name, w.position, w.email,
    c.id, c.name, c.description, c.color
FROM tasks t
INNER JOIN workers w ON t.worker_id = w.id
LEFT JOIN categories c ON t.category_id = c.id
WHERE t.id = ?
```

**Result:** A `TaskDetail` object containing:
- Complete task information
- Associated worker details (name, position, email)
- Category information (if assigned)

### Additional JOIN Examples:
1. `getAllTasksWithDetails()` - Gets all tasks with worker and category data
2. `getTasksByCategory(categoryId)` - Filters tasks by category with JOIN

---

## Requirement #2: Design Patterns

### Singleton Pattern
**Class:** `Database.java`

```java
private static Database instance;

private Database() {} // Private constructor

public static synchronized Database getInstance() {
    if (instance == null) {
        instance = new Database();
    }
    return instance;
}
```

**Why Singleton?**
- Ensures only ONE database connection manager exists
- Thread-safe with synchronized keyword
- Prevents multiple connection pools
- Centralizes database configuration

**Usage:**
```java
Connection conn = Database.getInstance().getConnection();
```

### Factory Pattern Concept
While not explicitly implemented as a separate Factory class, the repository classes follow Factory-like patterns:

**Repository Pattern (Factory-like):**
```java
// TaskRepository acts as a factory for Task objects
Task task = taskRepo.getTaskById(1);
List<Task> tasks = taskRepo.getAllTasks();
```

Each repository handles the creation and retrieval of domain objects from the database.

---

## Requirement #3: Lambda Expressions

### Example 1: Filtering Tasks by Status
**Location:** `TaskRepository.java`

```java
public List<Task> getTasksByStatus(String status) {
    return getAllTasks().stream()
        .filter(task -> task.getStatus().equalsIgnoreCase(status))
        .collect(Collectors.toList());
}
```

### Example 2: Finding Overdue Tasks
```java
public List<Task> getOverdueTasks() {
    LocalDate today = LocalDate.now();
    return getAllTasks().stream()
        .filter(task -> task.getDeadline().isBefore(today))
        .filter(task -> !task.getStatus().equalsIgnoreCase("Completed"))
        .sorted((t1, t2) -> t1.getDeadline().compareTo(t2.getDeadline()))
        .collect(Collectors.toList());
}
```

**Lambda Components:**
- `filter(task -> ...)` - Predicate lambda for filtering
- `sorted((t1, t2) -> ...)` - Comparator lambda for sorting
- `forEach(System.out::println)` - Method reference (special lambda)

### Example 3: Worker Search
**Location:** `WorkerRepository.java`

```java
public List<Worker> searchWorkersByName(String namePattern) {
    return getAllWorkers().stream()
        .filter(worker -> worker.getName().toLowerCase()
            .contains(namePattern.toLowerCase()))
        .sorted((w1, w2) -> w1.getName().compareTo(w2.getName()))
        .collect(Collectors.toList());
}
```

### Example 4: Method References in Controller
```java
workers.forEach(System.out::println);
```

This is a method reference, which is a shorthand for the lambda: `worker -> System.out.println(worker)`

---

## Requirement #4: SOLID Principles

### S - Single Responsibility Principle
Each class has ONE reason to change:

**Examples:**
- `TaskRepository` - Only handles task data access
- `WorkerRepository` - Only handles worker data access
- `Validator` - Only handles validation logic
- `SecurityService` - Only handles authorization
- `Database` - Only manages database connections
- `ReminderController` - Only coordinates between UI and business logic

### O - Open/Closed Principle
Classes are open for extension but closed for modification:

**Example:** Adding a new filter method doesn't require changing existing code:
```java
// Can add new methods without modifying existing ones
public List<Task> getTasksByCustomCriteria(Predicate<Task> criteria) {
    return getAllTasks().stream()
        .filter(criteria)
        .collect(Collectors.toList());
}
```

### L - Liskov Substitution Principle
Though not using interfaces here, the design allows for it:

**Future Extension:**
```java
interface Repository<T> {
    void add(T entity);
    void delete(int id);
    List<T> getAll();
}

// TaskRepository could implement Repository<Task>
// WorkerRepository could implement Repository<Worker>
```

### I - Interface Segregation Principle
Each repository has specific methods for its entity:
- `TaskRepository` - Task-specific methods
- `WorkerRepository` - Worker-specific methods
- No forced implementation of irrelevant methods

### D - Dependency Inversion Principle
High-level modules (Controller) depend on abstractions (Repositories), not concrete implementations:

```java
public class ReminderController {
    // Depends on repository abstractions
    private final WorkerRepository workerRepo;
    private final TaskRepository taskRepo;
    
    // Could easily swap implementations
}
```

**Benefits:**
- Easy to test with mock repositories
- Can change database implementation without changing controller
- Loose coupling between layers

---

## Requirement #5: Role Management (Secured Endpoints)

### User Roles
**Enum:** `UserRole.java`
- **ADMIN** - Full system access
- **MANAGER** - Can manage workers and tasks
- **EDITOR** - Can modify tasks
- **VIEWER** - Read-only access

### Security Service
**Class:** `SecurityService.java`

Provides authorization checks:

```java
public void requireAdmin(User user) throws AuthorizationException
public void requireManagerOrHigher(User user) throws AuthorizationException
public void requireEditorOrHigher(User user) throws AuthorizationException
public void requireAuthenticated(User user) throws AuthorizationException
```

### Secured Endpoints Examples

#### Admin Only:
```java
public void addUser(...) {
    securityService.requireAdmin(currentUser);
    // Only executes if user is ADMIN
}
```

#### Manager or Admin:
```java
public void addWorker(...) {
    securityService.requireManagerOrHigher(currentUser);
    // Only executes if user is MANAGER or ADMIN
}
```

#### Editor, Manager, or Admin:
```java
public void addTask(...) {
    securityService.requireTaskModificationPermission(currentUser);
    // Only executes if user is EDITOR, MANAGER, or ADMIN
}
```

#### All Authenticated Users:
```java
public void showTasks() {
    securityService.requireAuthenticated(currentUser);
    // Any logged-in user can view
}
```

### Access Control Matrix

| Operation | ADMIN | MANAGER | EDITOR | VIEWER |
|-----------|-------|---------|--------|--------|
| View Tasks | ✓ | ✓ | ✓ | ✓ |
| Add Task | ✓ | ✓ | ✓ | ✗ |
| Delete Task | ✓ | ✓ | ✓ | ✗ |
| View Workers | ✓ | ✓ | ✓ | ✓ |
| Add Worker | ✓ | ✓ | ✗ | ✗ |
| Delete Worker | ✓ | ✗ | ✗ | ✗ |
| Manage Users | ✓ | ✗ | ✗ | ✗ |

---

## Requirement #6: Data Validation

### Validator Class
**Location:** `Validator.java`

Provides comprehensive validation methods:

### Email Validation
```java
public static void validateEmail(String email) throws ValidationException {
    Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    if (!EMAIL_PATTERN.matcher(email).matches()) {
        throw new ValidationException("Invalid email format");
    }
}
```

### Name Validation
```java
public static void validateName(String name) throws ValidationException {
    if (name == null || name.trim().isEmpty()) {
        throw new ValidationException("Name cannot be empty");
    }
    if (name.length() < 2 || name.length() > 50) {
        throw new ValidationException("Name must be 2-50 characters");
    }
}
```

### Deadline Validation
```java
public static void validateDeadline(LocalDate deadline) throws ValidationException {
    if (deadline.isBefore(LocalDate.now())) {
        throw new ValidationException("Deadline cannot be in the past");
    }
}
```

### Status Validation
```java
public static void validateStatus(String status) throws ValidationException {
    String[] validStatuses = {"Pending", "In Progress", "Completed", "Cancelled"};
    // Validates against allowed values
}
```

### Usage in Repositories
```java
public void addTask(Task task) throws ValidationException {
    Validator.validateTitle(task.getTitle());
    Validator.validateDeadline(task.getDeadline());
    Validator.validateStatus(task.getStatus());
    Validator.validatePriority(task.getPriority());
    // Then proceed with database operation
}
```

### Validation Rules Summary

| Field | Rules |
|-------|-------|
| Email | Must match email pattern |
| Name | 2-50 characters, letters and spaces only |
| Title | 3-100 characters |
| Deadline | Cannot be in the past |
| Status | Must be: Pending, In Progress, Completed, or Cancelled |
| Priority | Must be: Low, Medium, High, or Critical |
| ID | Must be positive integer |

---

## Requirement #7: Categories for Entities

### Category Entity
**Class:** `Category.java`

```java
public class Category {
    private int id;
    private String name;
    private String description;
    private String color;
}
```

### Enhanced Task Entity
Tasks now support categories:

```java
public class Task {
    private Integer categoryId; // Nullable - tasks can exist without category
}
```

### Category Operations

#### 1. Add Category
```java
controller.addCategory("Development", "Software dev tasks", "#3498db");
```

#### 2. Assign Category to Task
```java
controller.addTask(workerId, categoryId, title, description, deadline, priority);
```

#### 3. Get Tasks by Category (with JOIN)
```java
public List<TaskDetail> getTasksByCategory(int categoryId) {
    // Returns tasks with full worker and category details
}
```

### Database Schema
```sql
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    color VARCHAR(20)
);

CREATE TABLE tasks (
    ...
    category_id INTEGER,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);
```

### Sample Categories
- **Development** - Software development tasks (#3498db)
- **Design** - UI/UX design tasks (#e74c3c)
- **Testing** - QA tasks (#2ecc71)
- **Documentation** - Technical writing (#f39c12)
- **Meeting** - Meetings and presentations (#9b59b6)

---

## Database Setup

### 1. Create Database
```bash
psql -U postgres
CREATE DATABASE reminder_db;
\c reminder_db
```

### 2. Run Initialization Script
```bash
psql -U postgres -d reminder_db -f init_database.sql
```

### 3. Verify Tables
```sql
\dt  -- List all tables
SELECT * FROM users;
SELECT * FROM workers;
SELECT * FROM categories;
SELECT * FROM tasks;
```

---

## Running the Application

### 1. Compile
```bash
javac *.java
```

### 2. Run
```bash
java Main
```

### 3. Login
Use one of these test accounts:
- **admin** / 12345678 (Full access)
- **manager1** / 12345678 (Manager access)
- **editor1** / 12345678 (Editor access)
- **viewer1** / 12345678 (View only)

---

## Testing All Requirements

### Test #1: JOINs
1. Login as any user
2. Navigate to: 2. Task Management → 3. Show Full Task Description
3. Enter a task ID (e.g., 1)
4. Observe combined data from tasks, workers, and categories

### Test #2: Design Patterns
- Singleton is automatically used (Database.getInstance())
- Try creating tasks, workers - all use same database instance

### Test #3: Lambda Expressions
1. Navigate to: 2. Task Management → 9. Show Overdue Tasks
2. Navigate to: 2. Task Management → 7. Show Tasks by Status
3. Navigate to: 1. Worker Management → 4. Search Workers by Position

### Test #4: SOLID Principles
- Review code structure
- Note separation of concerns
- Each class has single responsibility

### Test #5: Role Management
1. Login as **viewer1** (VIEWER role)
2. Try to add a task - should be denied
3. Logout and login as **editor1** (EDITOR role)
4. Try to add a task - should succeed
5. Try to delete a worker - should be denied
6. Login as **admin** - all operations should work

### Test #6: Data Validation
1. Try to add a worker with invalid email (e.g., "notanemail")
2. Try to add a task with past deadline
3. Try to set invalid status (e.g., "InvalidStatus")
4. Observe validation error messages

### Test #7: Categories
1. Navigate to: 3. Category Management → 1. Show All Categories
2. Add a new category
3. Add a task and assign it to a category
4. Navigate to: 2. Task Management → 10. Show Tasks by Category

---

## Key Features Demonstration

### Complex JOIN Query Example
```sql
-- This query is used in getFullTaskDescription()
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
```

### Lambda Expression Example
```java
// Filter and sort overdue tasks
LocalDate today = LocalDate.now();
return getAllTasks().stream()
    .filter(task -> task.getDeadline().isBefore(today))
    .filter(task -> !task.getStatus().equalsIgnoreCase("Completed"))
    .sorted((t1, t2) -> t1.getDeadline().compareTo(t2.getDeadline()))
    .collect(Collectors.toList());
```

### Role-Based Security Example
```java
public void deleteWorker(int id) {
    try {
        // Secured endpoint - Admin only
        securityService.requireDeleteWorkerPermission(currentUser);
        workerRepo.deleteWorker(id);
    } catch (AuthorizationException e) {
        System.out.println("ERROR: " + e.getMessage());
    }
}
```

---

## Additional Improvements Made

1. **Error Handling**
   - Custom exceptions (ValidationException, AuthorizationException)
   - Try-catch blocks in all operations
   - User-friendly error messages

2. **Code Organization**
   - Separation of concerns
   - Repository pattern for data access
   - Service layer for business logic
   - Controller for UI coordination

3. **Database Design**
   - Foreign key constraints
   - Indexes for JOIN performance
   - Check constraints for data integrity
   - Cascade delete for referential integrity

4. **User Experience**
   - Comprehensive menu system
   - Clear prompts and messages
   - Role-based menu options
   - Input validation feedback

---

## Conclusion

This enhanced Task Reminder System demonstrates all 7 required features:

✅ **JOINs** - Complex queries combining tasks, workers, and categories
✅ **Design Patterns** - Singleton pattern for database management
✅ **Lambda Expressions** - Filtering, sorting, and searching operations
✅ **SOLID Principles** - Well-structured, maintainable code
✅ **Role Management** - Secure endpoints with role-based access control
✅ **Data Validation** - Comprehensive input validation
✅ **Categories** - Task categorization system with full integration

The system is production-ready with proper error handling, security, and data integrity.
