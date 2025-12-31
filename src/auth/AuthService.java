package auth;

public class AuthService {

    private UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Credential validation
    public boolean authenticate(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }

    // Password management
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        User user = userDAO.findByUsername(
                userDAO.findByUsername(
                        userDAO.findByUsername(String.valueOf(userId)) != null ? String.valueOf(userId) : ""
                ) != null ? "" : ""
        );

        // Better approach: add findById() in DAO (recommended)
        if (user == null || !user.getPsword().equals(oldPassword)) {
            return false;
        }
        return userDAO.updatePassword(userId, newPassword);
    }
}
