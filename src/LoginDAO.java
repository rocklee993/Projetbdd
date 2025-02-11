import java.sql.*;

public class LoginDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/projetreservations";  // Replace with your DB details
    private static final String USER = "root";  // Replace with your DB username
    private static final String PASSWORD = "";  // Replace with your DB password

    // Method to establish connection to the database
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method to verify user login
    public static String verifyUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {  // If a row is found, login is valid
                if (email.equalsIgnoreCase("admin@uha.fr")) {
                    return "admin";  // Return admin role
                }
                return "user";  // Return normal user role
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "invalid";  // Return invalid if login fails
    }
}
