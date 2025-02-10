import java.sql.*;

public class SignupDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/projetreservations";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    // Method to insert a new user into the database
    public static boolean registerUser(String firstName, String lastName, String email, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Establish connection to the database
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // Check if the email already exists
            String checkEmailQuery = "SELECT * FROM users WHERE email = ?";
            stmt = conn.prepareStatement(checkEmailQuery);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Email already exists
                return false;
            }

            // Insert new user into the database
            String insertQuery = "INSERT INTO users (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, password); // In real scenarios, you'd hash the password before storing it

            // Execute the insert query
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
