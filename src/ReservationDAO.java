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
    }


    public static Vol getVolDetails(int flightId) {
        Vol vol = null;
        String sql = "SELECT id, date_depart, date_arrivee, lieu_depart, lieu_arrivee FROM vols WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, flightId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                vol = new Vol();
                vol.setId(rs.getInt("id"));
                vol.setDateDepart(rs.getTimestamp("date_depart").toLocalDateTime());
                vol.setDateArrivee(rs.getTimestamp("date_arrivee").toLocalDateTime());
                vol.setLieuDepart(rs.getString("lieu_depart"));
                vol.setLieuArrivee(rs.getString("lieu_arrivee"));
            }

        } catch (SQLException e) {
            System.err.println("Error getting flight details: " + e.getMessage());
            e.printStackTrace(); // Very important to print the stack trace!
        }

        return vol;
    }
    
    public static List<Reservation> getReservationsByUserId(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT id, user_id, flight_id, reservation_date, status FROM reservations WHERE user_id = ?"; //Sélectionner l'ID!

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation(
                    rs.getInt("user_id"),
                    rs.getInt("flight_id"),
                    rs.getDate("reservation_date").toString(), // Conversion de Date à String
                    rs.getString("status")
                );
                reservation.setId(rs.getInt("id")); // Définir l'ID de la réservation ici!
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
}