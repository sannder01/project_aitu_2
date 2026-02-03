/**
 * Security service for role-based access control
 * Requirement #5: Role Management (secured endpoints)
 * SOLID: Single Responsibility - handles only authorization logic
 */
public class SecurityService {
    
    /**
     * Checks if user has permission to perform admin operations
     * @param user current user
     * @throws AuthorizationException if user doesn't have permission
     */
    public void requireAdmin(User user) throws AuthorizationException {
        if (user == null) {
            throw new AuthorizationException("User not authenticated");
        }
        if (user.getRole() != UserRole.ADMIN) {
            throw new AuthorizationException(
                "Access denied. Admin role required. Current role: " + user.getRole()
            );
        }
    }

    /**
     * Checks if user has permission to manage workers
     * @param user current user
     * @throws AuthorizationException if user doesn't have permission
     */
    public void requireManagerOrHigher(User user) throws AuthorizationException {
        if (user == null) {
            throw new AuthorizationException("User not authenticated");
        }
        if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.MANAGER) {
            throw new AuthorizationException(
                "Access denied. Manager or Admin role required. Current role: " + user.getRole()
            );
        }
    }

    /**
     * Checks if user has permission to edit content
     * @param user current user
     * @throws AuthorizationException if user doesn't have permission
     */
    public void requireEditorOrHigher(User user) throws AuthorizationException {
        if (user == null) {
            throw new AuthorizationException("User not authenticated");
        }
        if (user.getRole() == UserRole.VIEWER) {
            throw new AuthorizationException(
                "Access denied. Editor, Manager, or Admin role required. Current role: " + user.getRole()
            );
        }
    }

    /**
     * Checks if user can view content (all authenticated users)
     * @param user current user
     * @throws AuthorizationException if user doesn't have permission
     */
    public void requireAuthenticated(User user) throws AuthorizationException {
        if (user == null) {
            throw new AuthorizationException("User not authenticated");
        }
    }

    /**
     * Checks if user can delete workers (Admin only)
     * @param user current user
     * @throws AuthorizationException if user doesn't have permission
     */
    public void requireDeleteWorkerPermission(User user) throws AuthorizationException {
        requireAdmin(user);
    }

    /**
     * Checks if user can add workers (Manager or Admin)
     * @param user current user
     * @throws AuthorizationException if user doesn't have permission
     */
    public void requireAddWorkerPermission(User user) throws AuthorizationException {
        requireManagerOrHigher(user);
    }

    /**
     * Checks if user can modify tasks (Editor, Manager, or Admin)
     * @param user current user
     * @throws AuthorizationException if user doesn't have permission
     */
    public void requireTaskModificationPermission(User user) throws AuthorizationException {
        requireEditorOrHigher(user);
    }
}
