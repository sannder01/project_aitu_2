/**
 * User roles for role-based access control
 * Requirement #5: Role Management
 */
public enum UserRole {
    ADMIN("Admin"),
    MANAGER("Manager"),
    EDITOR("Editor"),
    VIEWER("Viewer");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
