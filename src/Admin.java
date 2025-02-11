// The Admin class extends the Person class, adding administrative functionality
public class Admin extends Person {

    // Constructor for Admin, initializes the Admin object with details inherited from Person
    public Admin(int id, String prenom, String nom, String email, String password) {
        super(id, prenom, nom, email, password); // Calls the constructor of the Person class
    }

    // Method to create a flight, only accessible by Admin
    public void createFlight() {
        System.out.println("Admin: Flight created."); // Simulate the action of creating a flight
    }

    // Method to modify an existing flight, only accessible by Admin
    public void modifyFlight() {
        System.out.println("Admin: Flight modified."); // Simulate the action of modifying a flight
    }

    // Method to delete a flight, only accessible by Admin
    public void deleteFlight() {
        System.out.println("Admin: Flight deleted."); // Simulate the action of deleting a flight
    }
}
