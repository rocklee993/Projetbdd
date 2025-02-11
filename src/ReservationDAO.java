import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant l'interaction avec la base de données pour la gestion des réservations.
 */
public class ReservationDAO {
	  private static final String URL = "jdbc:mysql://localhost:3306/projetreservations";
	    private static final String USER = "root";
	    private static final String PASSWORD = "";

	    public static List<Vol> rechercherVols(String depart, String arrivee, String date) {
	        List<Vol> vols = new ArrayList<>();
	        String query = "SELECT * FROM vols WHERE lieu_depart = ? AND lieu_arrivee = ? AND DATE(date_depart) = ?";

	        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
	             PreparedStatement stmt = conn.prepareStatement(query)) {
	            
	            stmt.setString(1, depart);
	            stmt.setString(2, arrivee);
	            stmt.setString(3, date);

	            ResultSet rs = stmt.executeQuery();

	            while (rs.next()) {
	                Vol vol = new Vol(
	                    rs.getInt("id"),
	                    rs.getTimestamp("date_depart").toLocalDateTime(),
	                    rs.getTimestamp("date_arrivee").toLocalDateTime(),
	                    rs.getString("lieu_depart"),
	                    rs.getString("lieu_arrivee")
	                );
	                vols.add(vol);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return vols;
	    }

	    public static boolean createReservation(int flightId) {
	        if (Session.userId == -1) {
	            System.out.println("Aucun utilisateur connecté !");
	            return false;
	        }

	        String query = "INSERT INTO reservations (user_id, flight_id, reservation_date, status) VALUES (?, ?, ?, ?)";

	        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
	             PreparedStatement stmt = conn.prepareStatement(query)) {
	            
	        	 stmt.setInt(1, Session.userId); // ID de l'utilisateur connecté
	             stmt.setInt(2, flightId); // ID du vol
	             stmt.setDate(3, java.sql.Date.valueOf(LocalDateTime.now().toLocalDate())); // Date actuelle
	             stmt.setString(4, "Confirmée"); // Statut de réservation
	             
	             int rowsInserted = stmt.executeUpdate();
	             if (rowsInserted > 0) {
	                 System.out.println("✅ Réservation enregistrée dans la base de données !");
	                 return true;
	             } else {
	                 System.out.println("⚠️ Aucune ligne insérée.");
	                 return false;
	             }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }}