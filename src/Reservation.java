import java.time.LocalDate;

/**
 * Classe représentant une réservation de vol.
 */
class Reservation {
    private int id;
    private int userId;
    private int flightId;
    private String reservationDate;
    private String status;

    public Reservation(int userId, int flightId, String reservationDate, String status) {
        this.userId = userId;
        this.flightId = flightId;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    // Getters
    public int getUserId() { return userId; }
    public int getFlightId() { return flightId; }
    public String getReservationDate() { return reservationDate; }
    public String getStatus() { return status; }


    @Override
    public String toString() {
        return "Reservation [id=" + id + 
               ", userId=" + userId + 
               ", flightId=" + flightId + 
               ", reservationDate=" + reservationDate + 
               ", status=" + status + "]";
    }
}