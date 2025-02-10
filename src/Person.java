public class Person {
    private int id;
    private String prenom;  // First name
    private String nom;     // Last name
    private String email;   // Email
    private String password;

    public Person(int id, String prenom, String nom, String email, String password) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean authenticate(String inputPassword) {
        return password.equals(inputPassword);
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + prenom + " " + nom + ", Email: " + email;
    }
}
