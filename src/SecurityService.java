
public class SecurityService {
    

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


    public void requireAuthenticated(User user) throws AuthorizationException {
        if (user == null) {
            throw new AuthorizationException("User not authenticated");
        }
    }


    public void requireDeleteWorkerPermission(User user) throws AuthorizationException {
        requireAdmin(user);
    }


    public void requireAddWorkerPermission(User user) throws AuthorizationException {
        requireManagerOrHigher(user);
    }


    public void requireTaskModificationPermission(User user) throws AuthorizationException {
        requireEditorOrHigher(user);
    }
}
