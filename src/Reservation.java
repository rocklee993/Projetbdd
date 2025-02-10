public class Reservation {
    private int reservationId;
    private Person user; // the user who made the reservation
    private Vol vol; // the flight associated with the reservation
    private String status; // e.g., "Booked", "Checked In", etc.

    public Reservation(int reservationId, Person user, Vol vol) {
        this.reservationId = reservationId;
        this.user = user;
        this.vol = vol;
        this.status = "Booked"; // Initial status is 'Booked'
    }

    public int getReservationId() {
        return reservationId;
    }

    public Person getUser() {
        return user;
    }

    public Vol getVol() {
        return vol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId + "\n" +
               "User: " + user.getPrenom() + " " + user.getNom() + "\n" +
               "Flight: " + vol.getId() + "\n" +
               "Status: " + status;
    }
}
