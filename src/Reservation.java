class Reservation {
    // Déclaration des variables (champs) de la classe
    private int id; // Identifiant unique de la réservation
    private int userId; // Identifiant de l'utilisateur qui a fait la réservation
    private int flightId; // Identifiant du vol réservé
    private String reservationDate; // Date à laquelle la réservation a été faite (format texte)
    private String status; // Statut de la réservation (ex: "Confirmée", "Annulée")
    private String lieuDepart; // Lieu de départ du vol
    private String lieuArrivee; // Lieu d'arrivée du vol
    private String dateDepart; // Date et heure de départ du vol (format texte)
    private String dateArrivee; // Date et heure d'arrivée du vol (format texte)

    // Constructeur de la classe
    // Il permet de créer un objet Reservation en fournissant les informations nécessaires
    public Reservation(int userId, int flightId, String reservationDate, String status,
                       String lieuDepart, String lieuArrivee, String dateDepart, String dateArrivee) {
        // Initialisation des variables avec les valeurs fournies
        this.userId = userId; // L'ID de l'utilisateur est enregistré
        this.flightId = flightId; // L'ID du vol est enregistré
        this.reservationDate = reservationDate; // La date de réservation est enregistrée
        this.status = status; // Le statut de la réservation est enregistré
        this.lieuDepart = lieuDepart; // Le lieu de départ est enregistré
        this.lieuArrivee = lieuArrivee; // Le lieu d'arrivée est enregistré
        this.dateDepart = dateDepart; // La date de départ est enregistrée
        this.dateArrivee = dateArrivee; // La date d'arrivée est enregistrée
    }

    // Getters (accesseurs)
    // Ce sont des méthodes qui permettent de récupérer la valeur d'une variable privée
    public int getId() { return id; } // Retourne l'ID de la réservation
    public int getUserId() { return userId; } // Retourne l'ID de l'utilisateur
    public int getFlightId() { return flightId; } // Retourne l'ID du vol
    public String getReservationDate() { return reservationDate; } // Retourne la date de réservation
    public String getStatus() { return status; } // Retourne le statut de la réservation
    public String getLieuDepart() { return lieuDepart; } // Retourne le lieu de départ
    public String getLieuArrivee() { return lieuArrivee; } // Retourne le lieu d'arrivée
    public String getDateDepart() { return dateDepart; } // Retourne la date de départ
    public String getDateArrivee() { return dateArrivee; } // Retourne la date d'arrivée

    // Setter (mutateur)
    // C'est une méthode qui permet de modifier la valeur d'une variable privée
    public void setId(int id) {
        this.id = id; // Modifie l'ID de la réservation
    }

    // Méthode toString
    // Elle permet d'afficher les informations de l'objet Reservation sous forme de texte
    @Override
    public String toString() {
        return "Reservation [id=" + id + // Affiche l'ID
               ", userId=" + userId + // Affiche l'ID de l'utilisateur
               ", flightId=" + flightId + // Affiche l'ID du vol
               ", reservationDate=" + reservationDate + // Affiche la date de réservation
               ", status=" + status + // Affiche le statut
               ", lieuDepart=" + lieuDepart + // Affiche le lieu de départ
               ", lieuArrivee=" + lieuArrivee + // Affiche le lieu d'arrivée
               ", dateDepart=" + dateDepart + // Affiche la date de départ
               ", dateArrivee=" + dateArrivee + "]"; // Affiche la date d'arrivée
    }
}