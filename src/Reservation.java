class Reservation {
    private int id;
    private int userId;
    private int flightId;
    private String reservationDate;
    private String status;
    private String lieuDepart;
    private String lieuArrivee;
    private String dateDepart;
    private String dateArrivee;

    public Reservation(int userId, int flightId, String reservationDate, String status,
                       String lieuDepart, String lieuArrivee, String dateDepart, String dateArrivee) {
        this.userId = userId;
        this.flightId = flightId;
        this.reservationDate = reservationDate;
        this.status = status;
        this.lieuDepart = lieuDepart;
        this.lieuArrivee = lieuArrivee;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getFlightId() { return flightId; }
    public String getReservationDate() { return reservationDate; }
    public String getStatus() { return status; }
    public String getLieuDepart() { return lieuDepart; }
    public String getLieuArrivee() { return lieuArrivee; }
    public String getDateDepart() { return dateDepart; }
    public String getDateArrivee() { return dateArrivee; }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Reservation [id=" + id +
               ", userId=" + userId +
               ", flightId=" + flightId +
               ", reservationDate=" + reservationDate +
               ", status=" + status +
               ", lieuDepart=" + lieuDepart +
               ", lieuArrivee=" + lieuArrivee +
               ", dateDepart=" + dateDepart +
               ", dateArrivee=" + dateArrivee + "]";
    }
}