public class Admin extends Person {

    public Admin(int id, String prenom, String nom, String email, String password) {
        super(id, prenom, nom, email, password);
    }

    // Admin can create, modify, and delete flights
    public void createFlight() {
        System.out.println("Admin: Flight created.");
    }

    public void modifyFlight() {
        System.out.println("Admin: Flight modified.");
    }

    public void deleteFlight() {
        System.out.println("Admin: Flight deleted.");
    }
}
