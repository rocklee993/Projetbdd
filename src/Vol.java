import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Classe représentant un vol.
 */
public class Vol {

    private int id; // Identifiant du vol
    private LocalDateTime dateDepart; // Date et heure de départ
    private LocalDateTime dateArrivee; // Date et heure d'arrivée
    private String lieuDepart; // Lieu de départ
    private String lieuArrivee; // Lieu d'arrivée

    public Vol() {
        // Required for some frameworks and easier object creation in some cases
    }


    /**
     * Constructeur de la classe Vol.
     *
     * @param id          Identifiant du vol
     * @param dateDepart  Date et heure de départ
     * @param dateArrivee Date et heure d'arrivée
     * @param lieuDepart  Lieu de départ
     * @param lieuArrivee Lieu d'arrivée
     */
    public Vol(int id, LocalDateTime dateDepart, LocalDateTime dateArrivee, String lieuDepart, String lieuArrivee) {
        this.id = id;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;
        this.lieuDepart = lieuDepart;
        this.lieuArrivee = lieuArrivee;
    }

    /**
     * Retourne l'identifiant du vol.
     *
     * @return id du vol
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant du vol.
     *
     * @param id Identifiant du vol
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne la date et l'heure de départ.
     *
     * @return date de départ
     */
    public LocalDateTime getDateDepart() {
        return dateDepart;
    }

    /**
     * Définit la date et l'heure de départ.
     *
     * @param dateDepart Date et heure de départ
     */
    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }

    /**
     * Retourne la date et l'heure d'arrivée.
     *
     * @return date d'arrivée
     */
    public LocalDateTime getDateArrivee() {
        return dateArrivee;
    }

    /**
     * Définit la date et l'heure d'arrivée.
     *
     * @param dateArrivee Date et heure d'arrivée
     */
    public void setDateArrivee(LocalDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    /**
     * Retourne le lieu de départ.
     *
     * @return lieu de départ
     */
    public String getLieuDepart() {
        return lieuDepart;
    }

    /**
     * Définit le lieu de départ.
     *
     * @param lieuDepart Lieu de départ
     */
    public void setLieuDepart(String lieuDepart) {
        this.lieuDepart = lieuDepart;
    }

    /**
     * Retourne le lieu d'arrivée.
     *
     * @return lieu d'arrivée
     */
    public String getLieuArrivee() {
        return lieuArrivee;
    }

    /**
     * Définit le lieu d'arrivée.
     *
     * @param lieuArrivee Lieu d'arrivée
     */
    public void setLieuArrivee(String lieuArrivee) {
        this.lieuArrivee = lieuArrivee;
    }

    /**
     * Calcule et retourne la durée du vol en minutes.
     *
     * @return durée du vol en minutes
     */
    public long getDureeVol() {
        return Duration.between(dateDepart, dateArrivee).toMinutes();
    }

    /**
     * Redéfinition de la méthode toString pour afficher les informations du vol.
     */
    @Override
    public String toString() {
        return "Vol [ID: " + id + ", Départ: " + dateDepart + " (" + lieuDepart + "), Arrivée: " + dateArrivee + " ("
            + lieuArrivee + "), Durée: " + getDureeVol() + " minutes]";
    }
}