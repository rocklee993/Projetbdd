import java.sql.*;

/**
 * The LoginDAO class handles user authentication and interaction with the database.
 * It verifies if a user exists in the database and returns their role based on the email and password.
 */
public class LoginDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/projetreservations";  // Replace with your DB details
    private static final String USER = "root";  // Replace with your DB username
    private static final String PASSWORD = "";  // Replace with your DB password

    /**
     * The default constructor. No specific constructor is needed as the class provides static methods.
     */
    public LoginDAO() {
        // This is an explicit default constructor for clarity.
    }

    /**
     * Establishes a connection to the database.
     * 
     * @return A Connection object to interact with the database.
     * @throws SQLException If there is an error in establishing the connection.
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Verifies if the user exists in the database by checking their email and password.
     * 
     * @param email The email address of the user.
     * @param password The password of the user.
     * @return A string representing the user's role ("admin", "user", or "invalid").
     */
    public static String verifyUser(String email, String password) {
        String query = "SELECT id FROM users WHERE email = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {  
                int userId = resultSet.getInt("id");
                Session.userId = userId;  // Store the logged-in user's ID

                // Check if the user is an admin based on their email
                if (email.equalsIgnoreCase("admin@uha.fr")) {
                    return "admin";  
                }
                return "user";  
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "invalid";  // If the user doesn't exist, return "invalid"
    }
}
