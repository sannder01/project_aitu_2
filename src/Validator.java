import java.time.LocalDate;
import java.util.regex.Pattern;


public class Validator {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern NAME_PATTERN = 
        Pattern.compile("^[A-Za-z\\s]{2,50}$");


    public static void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format: " + email);
        }
    }


    public static void validateName(String name) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Name cannot be empty");
        }
        if (name.length() < 2 || name.length() > 50) {
            throw new ValidationException("Name must be between 2 and 50 characters");
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new ValidationException("Name can only contain letters and spaces");
        }
    }


    public static void validateTitle(String title) throws ValidationException {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Title cannot be empty");
        }
        if (title.length() < 3 || title.length() > 100) {
            throw new ValidationException("Title must be between 3 and 100 characters");
        }
    }


    public static void validateDeadline(LocalDate deadline) throws ValidationException {
        if (deadline == null) {
            throw new ValidationException("Deadline cannot be null");
        }
        if (deadline.isBefore(LocalDate.now())) {
            throw new ValidationException("Deadline cannot be in the past");
        }
    }


    public static void validateStatus(String status) throws ValidationException {
        if (status == null || status.trim().isEmpty()) {
            throw new ValidationException("Status cannot be empty");
        }
        String[] validStatuses = {"Pending", "In Progress", "Completed", "Cancelled"};
        boolean isValid = false;
        for (String validStatus : validStatuses) {
            if (validStatus.equalsIgnoreCase(status)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new ValidationException("Invalid status. Must be: Pending, In Progress, Completed, or Cancelled");
        }
    }


    public static void validatePriority(String priority) throws ValidationException {
        if (priority == null || priority.trim().isEmpty()) {
            throw new ValidationException("Priority cannot be empty");
        }
        String[] validPriorities = {"Low", "Medium", "High", "Critical"};
        boolean isValid = false;
        for (String validPriority : validPriorities) {
            if (validPriority.equalsIgnoreCase(priority)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new ValidationException("Invalid priority. Must be: Low, Medium, High, or Critical");
        }
    }


    public static void validateId(int id) throws ValidationException {
        if (id <= 0) {
            throw new ValidationException("ID must be a positive number");
        }
    }
}
