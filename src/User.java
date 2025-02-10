public class User extends Person {

    public User(int id, String prenom, String nom, String email, String password) {
        super(id, prenom, nom, email, password);
    }

    // User can only view flight details
    public void viewFlightDetails() {
        System.out.println("User: Viewing flight details.");
    }
}
