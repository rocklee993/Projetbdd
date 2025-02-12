import java.sql.*; // Importe les classes pour interagir avec une base de données SQL
import java.time.LocalDateTime; // Importe la classe pour manipuler les dates et heures
import java.time.format.DateTimeFormatter; // Importe la classe pour formater les dates et heures
import java.util.ArrayList; // Importe la classe pour créer des listes dynamiques
import java.util.List; // Importe l'interface List pour utiliser des listes

/**
 * Classe permettant l'interaction avec la base de données pour la gestion des vols.
 * DAO signifie Data Access Object (Objet d'Accès aux Données).
 * Son rôle est de gérer les opérations de lecture et d'écriture dans la base de données.
 */
public class VolDAO {
    // Déclaration des constantes pour la connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/projetreservations"; // L'adresse de la base de données
    private static final String USER = "root"; // Le nom d'utilisateur pour se connecter à la base de données
    private static final String PASSWORD = ""; // Le mot de passe pour se connecter à la base de données

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Définit le format des dates et heures

    /**
     * Ajoute un vol à la base de données.
     * @param vol L'objet Vol à ajouter.
     */
    public void ajouterVol(Vol vol) {
        String sql = "INSERT INTO vols (id,lieu_depart,lieu_arrivee,date_depart,date_arrivee) VALUES (?, ?, ?, ?, ?)"; // La requête SQL pour insérer un nouveau vol

        // Utilisation d'un try-with-resources pour gérer automatiquement la connexion et la requête
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); // Établit une connexion à la base de données
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prépare la requête SQL pour éviter les injections SQL

            stmt.setInt(1, vol.getId()); // Remplace le premier "?" par l'ID du vol
            stmt.setString(2, vol.getLieuDepart()); // Remplace le deuxième "?" par le lieu de départ
            stmt.setString(3, vol.getLieuArrivee()); // Remplace le troisième "?" par le lieu d'arrivée
            stmt.setString(4, vol.getDateDepart().format(FORMATTER)); // Remplace le quatrième "?" par la date et l'heure de départ formatées
            stmt.setString(5, vol.getDateArrivee().format(FORMATTER)); // Remplace le cinquième "?" par la date et l'heure d'arrivée formatées
            stmt.executeUpdate(); // Exécute la requête
            System.out.println("✅ Vol ajouté avec succès !"); // Affiche un message de succès
        } catch (SQLException e) { // Gère les exceptions SQL (erreurs lors de l'interaction avec la base de données)
            System.out.println("❌ Erreur lors de l'ajout du vol : " + e.getMessage()); // Affiche un message d'erreur
        }
    }

    /**
     * Modifie un vol existant dans la base de données.
     * @param vol L'objet Vol à modifier.
     */
    public void modifierVol(Vol vol) {
        String sql = "UPDATE vols SET date_depart=?, date_arrivee=?, lieu_depart=?, lieu_arrivee=? WHERE id=?"; // La requête SQL pour mettre à jour un vol

        // Utilisation d'un try-with-resources pour gérer automatiquement la connexion et la requête
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); // Établit une connexion à la base de données
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prépare la requête SQL pour éviter les injections SQL

            stmt.setString(1, vol.getDateDepart().format(FORMATTER)); // Remplace le premier "?" par la date et l'heure de départ formatées
            stmt.setString(2, vol.getDateArrivee().format(FORMATTER)); // Remplace le deuxième "?" par la date et l'heure d'arrivée formatées
            stmt.setString(3, vol.getLieuDepart()); // Remplace le troisième "?" par le lieu de départ
            stmt.setString(4, vol.getLieuArrivee()); // Remplace le quatrième "?" par le lieu d'arrivée
            stmt.setInt(5, vol.getId()); // Remplace le cinquième "?" par l'ID du vol

            int rowsUpdated = stmt.executeUpdate(); // Exécute la requête et récupère le nombre de lignes mises à jour
            if (rowsUpdated > 0) { // Si au moins une ligne a été mise à jour
                System.out.println("✅ Vol modifié avec succès !"); // Affiche un message de succès
            } else { // Si aucune ligne n'a été mise à jour
                System.out.println("⚠️ Aucun vol trouvé avec cet ID."); // Affiche un message d'avertissement
            }
        } catch (SQLException e) { // Gère les exceptions SQL (erreurs lors de l'interaction avec la base de données)
            System.out.println("❌ Erreur lors de la modification du vol : " + e.getMessage()); // Affiche un message d'erreur
        }
    }

    /**
     * Supprime un vol de la base de données.
     * @param id L'ID du vol à supprimer.
     */
    public void supprimerVol(int id) {
        String sql = "DELETE FROM vols WHERE id=?"; // La requête SQL pour supprimer un vol

        // Utilisation d'un try-with-resources pour gérer automatiquement la connexion et la requête
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); // Établit une connexion à la base de données
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prépare la requête SQL pour éviter les injections SQL

            stmt.setInt(1, id); // Remplace le premier "?" par l'ID du vol
            int rowsDeleted = stmt.executeUpdate(); // Exécute la requête et récupère le nombre de lignes supprimées

            if (rowsDeleted > 0) { // Si au moins une ligne a été supprimée
                System.out.println("✅ Vol supprimé avec succès !"); // Affiche un message de succès
            } else { // Si aucune ligne n'a été supprimée
                System.out.println("⚠️ Aucun vol trouvé avec cet ID."); // Affiche un message d'avertissement
            }
        } catch (SQLException e) { // Gère les exceptions SQL (erreurs lors de l'interaction avec la base de données)
            System.out.println("❌ Erreur lors de la suppression du vol : " + e.getMessage()); // Affiche un message d'erreur
        }
    }

    /**
     * Récupère un vol à partir de son ID.
     * @param id L'ID du vol à récupérer.
     * @return Un objet Vol contenant les informations du vol, ou null si le vol n'a pas été trouvé.
     */
    public Vol getVol(int id) {
        String sql = "SELECT * FROM vols WHERE id=?"; // La requête SQL pour récupérer un vol
        Vol vol = null; // Initialise l'objet Vol à null

        // Utilisation d'un try-with-resources pour gérer automatiquement la connexion et la requête
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); // Établit une connexion à la base de données
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prépare la requête SQL pour éviter les injections SQL

            stmt.setInt(1, id); // Remplace le premier "?" par l'ID du vol
            ResultSet rs = stmt.executeQuery(); // Exécute la requête et récupère les résultats

            if (rs.next()) { // Si un vol a été trouvé
                // Récupère les informations du vol
                LocalDateTime dateDepart = LocalDateTime.parse(rs.getString("date_depart"), FORMATTER); // Récupère la date et l'heure de départ et les convertit en LocalDateTime
                LocalDateTime dateArrivee = LocalDateTime.parse(rs.getString("date_arrivee"), FORMATTER); // Récupère la date et l'heure d'arrivée et les convertit en LocalDateTime
                String lieuDepart = rs.getString("lieu_depart"); // Récupère le lieu de départ
                String lieuArrivee = rs.getString("lieu_arrivee"); // Récupère le lieu d'arrivée

                vol = new Vol(id, dateDepart, dateArrivee, lieuDepart, lieuArrivee); // Crée un nouvel objet Vol avec les informations récupérées
            }
        } catch (SQLException e) { // Gère les exceptions SQL (erreurs lors de l'interaction avec la base de données)
            System.out.println("❌ Erreur lors de la récupération du vol : " + e.getMessage()); // Affiche un message d'erreur
        }

        return vol; // Retourne l'objet Vol, ou null si le vol n'a pas été trouvé
    }

    /**
     * Récupère la liste de tous les vols.
     * @return Une liste d'objets Vol contenant les informations de tous les vols.
     */
    public List<Vol> getTousLesVols() {
        List<Vol> vols = new ArrayList<>(); // Crée une liste vide pour stocker les vols
        String sql = "SELECT * FROM vols"; // La requête SQL pour récupérer tous les vols

        // Utilisation d'un try-with-resources pour gérer automatiquement la connexion et la requête
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); // Établit une connexion à la base de données
             Statement stmt = conn.createStatement(); // Crée un objet Statement pour exécuter la requête
             ResultSet rs = stmt.executeQuery(sql)) { // Exécute la requête et récupère les résultats

            while (rs.next()) { // Parcours les résultats
                // Récupère les informations du vol
                int id = rs.getInt("id"); // Récupère l'ID du vol
                LocalDateTime dateDepart = LocalDateTime.parse(rs.getString("date_depart"), FORMATTER); // Récupère la date et l'heure de départ et les convertit en LocalDateTime
                LocalDateTime dateArrivee = LocalDateTime.parse(rs.getString("date_arrivee"), FORMATTER); // Récupère la date et l'heure d'arrivée et les convertit en LocalDateTime
                String lieuDepart = rs.getString("lieu_depart"); // Récupère le lieu de départ
                String lieuArrivee = rs.getString("lieu_arrivee"); // Récupère le lieu d'arrivée

                vols.add(new Vol(id, dateDepart, dateArrivee, lieuDepart, lieuArrivee)); // Crée un nouvel objet Vol avec les informations récupérées et l'ajoute à la liste
            }
        } catch (SQLException e) { // Gère les exceptions SQL (erreurs lors de l'interaction avec la base de données)
            System.out.println("❌ Erreur lors de la récupération des vols : " + e.getMessage()); // Affiche un message d'erreur
        }

        return vols; // Retourne la liste des vols
    }
}