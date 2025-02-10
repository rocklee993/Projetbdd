import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant l'interaction avec la base de données pour la gestion des vols.
 */
public class VolDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/projetreservations"; // Assure-toi que la BDD existe
    private static final String USER = "root"; // Mets ton username
    private static final String PASSWORD = ""; // Mets ton mot de passe

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * Ajoute un vol à la base de données.
     */
    public void ajouterVol(Vol vol) {
        String sql = "INSERT INTO vols (id,lieu_depart,lieu_arrivee,date_depart,date_arrivee) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vol.getId());
            stmt.setString(2, vol.getLieuDepart());
            stmt.setString(3, vol.getLieuArrivee());
            stmt.setString(4, vol.getDateDepart().format(FORMATTER));
            stmt.setString(5, vol.getDateArrivee().format(FORMATTER));
            stmt.executeUpdate();
            System.out.println("✅ Vol ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout du vol : " + e.getMessage());
        }
    }

    /**
     * Modifie un vol existant dans la base de données.
     */
    public void modifierVol(Vol vol) {
        String sql = "UPDATE vols SET date_depart=?, date_arrivee=?, lieu_depart=?, lieu_arrivee=? WHERE id=?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vol.getDateDepart().format(FORMATTER));
            stmt.setString(2, vol.getDateArrivee().format(FORMATTER));
            stmt.setString(3, vol.getLieuDepart());
            stmt.setString(4, vol.getLieuArrivee());
            stmt.setInt(5, vol.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Vol modifié avec succès !");
            } else {
                System.out.println("⚠️ Aucun vol trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification du vol : " + e.getMessage());
        }
    }

    /**
     * Supprime un vol de la base de données.
     */
    public void supprimerVol(int id) {
        String sql = "DELETE FROM vols WHERE id=?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✅ Vol supprimé avec succès !");
            } else {
                System.out.println("⚠️ Aucun vol trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression du vol : " + e.getMessage());
        }
    }

    /**
     * Récupère un vol à partir de son ID.
     */
    public Vol getVol(int id) {
        String sql = "SELECT * FROM vols WHERE id=?";
        Vol vol = null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalDateTime dateDepart = LocalDateTime.parse(rs.getString("date_depart"), FORMATTER);
                LocalDateTime dateArrivee = LocalDateTime.parse(rs.getString("date_arrivee"), FORMATTER);
                String lieuDepart = rs.getString("lieu_depart");
                String lieuArrivee = rs.getString("lieu_arrivee");

                vol = new Vol(id, dateDepart, dateArrivee, lieuDepart, lieuArrivee);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération du vol : " + e.getMessage());
        }

        return vol;
    }

    /**
     * Récupère la liste de tous les vols.
     */
    public List<Vol> getTousLesVols() {
        List<Vol> vols = new ArrayList<>();
        String sql = "SELECT * FROM vols";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDateTime dateDepart = LocalDateTime.parse(rs.getString("date_depart"), FORMATTER);
                LocalDateTime dateArrivee = LocalDateTime.parse(rs.getString("date_arrivee"), FORMATTER);
                String lieuDepart = rs.getString("lieu_depart");
                String lieuArrivee = rs.getString("lieu_arrivee");

                vols.add(new Vol(id, dateDepart, dateArrivee, lieuDepart, lieuArrivee));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des vols : " + e.getMessage());
        }

        return vols;
    }
}
