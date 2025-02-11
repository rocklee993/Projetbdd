// The Person class holds common information for a person, such as their ID, name, email, and password
public class Person {
    private int id;           // Unique identifier for the person
    private String prenom;    // First name of the person
    private String nom;       // Last name of the person
    private String email;     // Email address of the person
    private String password;  // Password for authentication

    // Constructor to initialize a Person object with all required attributes
    public Person(int id, String prenom, String nom, String email, String password) {
        this.id = id;          // Set the ID for the person
        this.prenom = prenom;  // Set the first name
        this.nom = nom;        // Set the last name
        this.email = email;    // Set the email address
        this.password = password; // Set the password
    }

    // Getter method to return the person's ID
    public int getId() {
        return id;
    }

    // Getter method to return the person's first name
    public String getPrenom() {
        return prenom;
    }

    // Getter method to return the person's last name
    public String getNom() {
        return nom;
    }

    // Getter method to return the person's email address
    public String getEmail() {
        return email;
    }

    // Getter method to return the person's password (for authentication purposes)
    public String getPassword() {
        return password;
    }

    // Method to authenticate the person by comparing the input password with the stored password
    public boolean authenticate(String inputPassword) {
        return password.equals(inputPassword); // Return true if passwords match
    }

    // Override the toString method to display a summary of the person (excluding password)
    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + prenom + " " + nom + ", Email: " + email;
    }
}
