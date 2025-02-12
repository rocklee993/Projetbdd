import java.sql.*; // Importe les classes nécessaires pour interagir avec une base de données SQL
import java.time.LocalDate; // Importe la classe pour manipuler des dates (sans heure)
import java.time.LocalDateTime; // Importe la classe pour manipuler des dates et heures
import java.time.format.DateTimeFormatter; // Importe la classe pour formater les dates et heures
import java.util.ArrayList; // Importe la classe pour créer des listes dynamiques
import java.util.List; // Importe l'interface List pour utiliser des listes

/**
 * Classe permettant l'interaction avec la base de données pour la gestion des réservations.
 * DAO signifie Data Access Object (Objet d'Accès aux Données).
 * Son rôle est de gérer les opérations de lecture et d'écriture dans la base de données.
 */
public class ReservationDAO {
    // Déclaration des constantes pour la connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/projetreservations"; // L'adresse de la base de données
    private static final String USER = "root"; // Le nom d'utilisateur pour se connecter à la base de données
    private static final String PASSWORD = ""; // Le mot de passe pour se connecter à la base de données

    /**
     * Recherche les vols disponibles en fonction du lieu de départ, du lieu d'arrivée et de la date.
     * @param depart Le lieu de départ du vol.
     * @param arrivee Le lieu d'arrivée du vol.
     * @param date La date du vol (au format YYYY-MM-DD).
     * @return Une liste d'objets Vol correspondant aux critères de recherche.
     */
    public static List<Vol> rechercherVols(String depart, String arrivee, String date) {
        List<Vol> vols = new ArrayList<>(); // Crée une liste vide pour stocker les vols trouvés
        String query = "SELECT * FROM vols WHERE lieu_depart = ? AND lieu_arrivee = ? AND DATE(date_depart) = ?"; // La requête SQL pour rechercher les vols

        // Utilisation d'un try-with-resources pour gérer automatiquement la connexion et la requête
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); // Établit une connexion à la base de données
             PreparedStatement stmt = conn.prepareStatement(query)) { // Prépare la requête SQL pour éviter les injections SQL

            stmt.setString(1, depart); // Remplace le premier "?" par le lieu de départ
            stmt.setString(2, arrivee); // Remplace le deuxième "?" par le lieu d'arrivée
            stmt.setString(3, date); // Remplace le troisième "?" par la date

            ResultSet rs = stmt.executeQuery(); // Exécute la requête et récupère les résultats

            // Parcours des résultats
            while (rs.next()) { // Tant qu'il y a des vols dans les résultats
                // Crée un nouvel objet Vol avec les informations extraites de la base de données
                Vol vol = new Vol(
                    rs.getInt("id"), // Récupère l'ID du vol
                    rs.getTimestamp("date_depart").toLocalDateTime(), // Récupère la date et l'heure de départ et les convertit en LocalDateTime
                    rs.getTimestamp("date_arrivee").toLocalDateTime(), // Récupère la date et l'heure d'arrivée et les convertit en LocalDateTime
                    rs.getString("lieu_depart"), // Récupère le lieu de départ
                    rs.getString("lieu_arrivee") // Récupère le lieu d'arrivée
                );
                vols.add(vol); // Ajoute le vol à la liste des vols trouvés
            }
        } catch (SQLException e) { // Gère les exceptions SQL (erreurs lors de l'interaction avec la base de données)
            e.printStackTrace(); // Affiche la trace de l'erreur pour faciliter le débogage
        }
        return vols; // Retourne la liste des vols trouvés
    }

    /**
     * Crée une nouvelle réservation dans la base de données.
     * @param flightId L'ID du vol à réserver.
     * @return true si la réservation a été créée avec succès, false sinon.
     */
    public static boolean createReservation(int flightId) {
        // Vérifie si un utilisateur est connecté (Session.userId != -1)
        if (Session.userId == -1) {
            System.out.println("Aucun utilisateur connecté !");
            return false; // Retourne false si aucun utilisateur n'est connecté
        }

        String query = "INSERT INTO reservations (user_id, flight_id, reservation_date, status) VALUES (?, ?, ?, ?)"; // La requête SQL pour insérer une nouvelle réservation

        // Utilisation d'un try-with-resources pour gérer automatiquement la connexion et la requête
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); // Établit une connexion à la base de données
             PreparedStatement stmt = conn.prepareStatement(query)) { // Prépare la requête SQL pour éviter les injections SQL

            stmt.setInt(1, Session.userId); // Remplace le premier "?" par l'ID de l'utilisateur connecté
            stmt.setInt(2, flightId); // Remplace le deuxième "?" par l'ID du vol
            stmt.setDate(3, java.sql.Date.valueOf(LocalDateTime.now().toLocalDate())); // Remplace le troisième "?" par la date actuelle
            stmt.setString(4, "Confirmée"); // Remplace le quatrième "?" par le statut "Confirmée"

            int rowsInserted = stmt.executeUpdate(); // Exécute la requête et récupère le nombre de lignes insérées
            if (rowsInserted > 0) { // Si au moins une ligne a été insérée
                System.out.println("✅ Réservation enregistrée dans la base de données !");
                return true; // Retourne true si la réservation a été créée avec succès
            } else { // Si aucune ligne n'a été insérée
                System.out.println("⚠️ Aucune ligne insérée.");
                return false; // Retourne false si la réservation n'a pas été créée
            }
        } catch (SQLException e) { // Gère les exceptions SQL (erreurs lors de l'interaction avec la base de données)
            e.printStackTrace(); // Affiche la trace de l'erreur pour faciliter le débogage
            return false; // Retourne false en cas d'erreur
        }
    }

    /**
     * Récupère les détails d'un vol à partir de son ID.
     * @param flightId L'ID du vol à récupérer.
     * @return Un objet Vol contenant les détails du vol, ou null si le vol n'a pas été trouvé.
     */
    public static Vol getVolDetails(int flightId) {
        Vol vol = null; // Initialise l'objet Vol à null
        String sql = "SELECT id, date_depart, date_arrivee, lieu_depart, lieu_arrivee FROM vols WHERE id = ?"; // La requête SQL pour récupérer les détails du vol

        // Utilisation d'un try-with-resources pour gérer automatiquement la connexion et la requête
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); // Établit une connexion à la base de données
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prépare la requête SQL pour éviter les injections SQL

            pstmt.setInt(1, flightId); // Remplace le premier "?" par l'ID du vol
            ResultSet rs = pstmt.executeQuery(); // Exécute la requête et récupère les résultats

            // Vérifie si un vol a été trouvé
            if (rs.next()) { // S'il y a un vol dans les résultats
                vol = new Vol(); // Crée un nouvel objet Vol
                vol.setId(rs.getInt("id")); // Récupère l'ID du vol
                vol.setDateDepart(rs.getTimestamp("date_depart").toLocalDateTime()); // Récupère la date et l'heure de départ et les convertit en LocalDateTime
                vol.setDateArrivee(rs.getTimestamp("date_arrivee").toLocalDateTime()); // Récupère la date et l'heure d'arrivée et les convertit en LocalDateTime
                vol.setLieuDepart(rs.getString("lieu_depart")); // Récupère le lieu de départ
                vol.setLieuArrivee(rs.getString("lieu_arrivee")); // Récupère le lieu d'arrivée
            }

        } catch (SQLException e) { // Gère les exceptions SQL (erreurs lors de l'interaction avec la base de données)
            System.err.println("Error getting flight details: " + e.getMessage()); // Affiche un message d'erreur
            e.printStackTrace(); // Very important to print the stack trace! // Affiche la trace de l'erreur pour faciliter le débogage
        }

        return vol; // Retourne l'objet Vol contenant les détails du vol, ou null si le vol n'a pas été trouvé
    }

     /**
     * Récupère la liste des réservations pour un utilisateur donné.
     * @param userId L'ID de l'utilisateur dont on veut récupérer les réservations.
     * @return Une liste d'objets Reservation contenant les informations des réservations de l'utilisateur.
     */
    public static List<Reservation> getReservationsByUserId(int userId) {
        List<Reservation> reservations = new ArrayList<>(); // Crée une liste vide pour stocker les réservations
        // La requête SQL pour récupérer les réservations de l'utilisateur avec les informations du vol associé
        String query = "SELECT r.id AS reservation_id, r.flight_id, r.reservation_date, r.status, " +
                       "v.lieu_depart, v.lieu_arrivee, v.date_depart, v.date_arrivee, r.user_id " + // Ajout de r.user_id
                       "FROM reservations r " + // Sélectionne les informations de la table "reservations" (alias "r")
                       "INNER JOIN vols v ON r.flight_id = v.id " + // Joint la table "reservations" avec la table "vols" (alias "v") en utilisant la colonne "flight_id"
                       "WHERE r.user_id = ?"; // Filtre les résultats pour ne récupérer que les réservations de l'utilisateur spécifié

        // Utilisation d'un try-with-resources pour gérer automatiquement la connexion et la requête
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); // Établit une connexion à la base de données
             PreparedStatement stmt = conn.prepareStatement(query)) { // Prépare la requête SQL pour éviter les injections SQL

            stmt.setInt(1, userId); // Remplace le premier "?" par l'ID de l'utilisateur
            ResultSet rs = stmt.executeQuery(); // Exécute la requête et récupère les résultats

            // Parcours des résultats
            while (rs.next()) { // Tant qu'il y a des réservations dans les résultats
                // Crée un nouvel objet Reservation avec les informations extraites de la base de données
                Reservation reservation = new Reservation(
                    rs.getInt("user_id"), // Récupère l'ID de l'utilisateur
                    rs.getInt("flight_id"), // Récupère l'ID du vol
                    rs.getDate("reservation_date").toString(), // Récupère la date de réservation et la convertit en String
                    rs.getString("status"), // Récupère le statut de la réservation
                    rs.getString("lieu_depart"), // Récupère le lieu de départ du vol
                    rs.getString("lieu_arrivee"), // Récupère le lieu d'arrivée du vol
                    rs.getTimestamp("date_depart").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), // Récupère la date et l'heure de départ du vol et les formate
                    rs.getTimestamp("date_arrivee").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) // Récupère la date et l'heure d'arrivée du vol et les formate
                );
                reservation.setId(rs.getInt("reservation_id")); // Récupère l'ID de la réservation
                reservations.add(reservation); // Ajoute la réservation à la liste
            }
        } catch (SQLException e) { // Gère les exceptions SQL (erreurs lors de l'interaction avec la base de données)
            e.printStackTrace(); // Affiche la trace de l'erreur pour faciliter le débogage
        }
        return reservations; // Retourne la liste des réservations
    }
    
    /**
     * Annule une réservation dans la base de données.
     *
     * @param reservationId L'ID de la réservation à annuler.
     * @return true si la réservation a été annulée avec succès, false sinon.
     */
    public static boolean annulerReservation(int reservationId) {
        // Vérifier si la réservation existe et si elle peut être annulée (date de départ > 24h)
        if (!peutAnnulerReservation(reservationId)) {
            System.out.println("⚠️ La réservation ne peut pas être annulée car le délai est dépassé.");
            return false;
        }

        String query = "UPDATE reservations SET status = 'Annulée' WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reservationId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Réservation annulée avec succès !");
                return true;
            } else {
                System.out.println("⚠️ Aucune réservation trouvée avec cet ID.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'annulation de la réservation : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Vérifie si une réservation peut être annulée (date de départ > 24h).
     *
     * @param reservationId L'ID de la réservation à vérifier.
     * @return true si la réservation peut être annulée, false sinon.
     */
    public static boolean peutAnnulerReservation(int reservationId) {
        String query = "SELECT v.date_depart FROM reservations r INNER JOIN vols v ON r.flight_id = v.id WHERE r.id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalDateTime dateDepart = rs.getTimestamp("date_depart").toLocalDateTime();
                LocalDateTime maintenantPlus24h = LocalDateTime.now().plusHours(24);
                return dateDepart.isAfter(maintenantPlus24h); // Vérifie si la date de départ est après maintenant + 24h
            } else {
                System.out.println("⚠️ Aucune réservation trouvée avec cet ID.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la vérification de la date de départ : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    
    
}