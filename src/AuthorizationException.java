/**
 * Custom exception for authorization errors
 * Requirement #5: Role Management
 */
public class AuthorizationException extends Exception {
    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
