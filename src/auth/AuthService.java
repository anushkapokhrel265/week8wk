package auth;

import db.DBConnection;

public class AuthService {

    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO(DBConnection.getConnection());
    }

    public boolean authenticate(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user == null) return false;
        return user.getPassword().equals(password);
    }

    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        User user = userDAO.findByUsername(getUsernameById(userId));
        if (user == null) return false;
        if (!user.getPassword().equals(oldPassword)) return false;
        return userDAO.updatePassword(userId, newPassword);
    }

    private String getUsernameById(int userId) {
        return null;
    }
}