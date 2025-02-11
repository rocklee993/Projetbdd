import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * AffichageVol is a GUI class used to display detailed information about a specific flight.
 * It shows the flight details in a non-editable text area, with a button to cancel the reservation.
 */
public class AffichageVol extends JFrame {

	/**
	 * Button to cancel the flight reservation.
	 */
	private JButton annulerButton;

	/**
	 * The flight object whose details are displayed in this window.
	 */
	private Vol volAAfficher;

	/**
	 * The text area used to display flight information.
	 */
	private JTextArea volInfoArea;

    /**
     * Constructs an instance of AffichageVol to display flight details.
     * 
     * @param vol the flight object to display.
     */
    public AffichageVol(Vol vol) {
        this.volAAfficher = vol;  // Store the Vol object to display

        setTitle("Informations de Vol");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setSize(400, 300);
        setLayout(new BorderLayout());

        volInfoArea = new JTextArea();
        volInfoArea.setEditable(false);
        volInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Use a fixed-width font
        afficherVol(vol); // Display the flight details

        annulerButton = new JButton("Annuler réservation");
        // Add action listener for the "Annuler réservation" button (implementation not shown here)
        // annulerButton.addActionListener(this);

        add(new JScrollPane(volInfoArea), BorderLayout.CENTER); // Add scroll pane for long text
        add(annulerButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Displays the details of a given flight.
     * 
     * @param vol the flight object whose details are to be displayed.
     */
    public void afficherVol(Vol vol) {
        if (vol != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            volInfoArea.setText(
                    "Vol " + vol.getId() + "\n" +
                            "Départ : " + vol.getDepart() + "\n" +
                            "Direction " + vol.getArrivee() + "\n" +
                            "Date de départ : " + vol.getDateDepart().format(formatter) + "\n" +
                            "Départ : " + vol.getHeureDepart() + "\n" +
                            "Arrivée : " + vol.getHeureArrivee() + "\n" +
                            "Classe : " + vol.getClasse()
            );
        }
    }

    /**
     * Inner class representing a Flight.
     * Contains the details of a flight, such as ID, departure and arrival information, date, time, and class.
     */
    class Vol {
        private int id; // Add an ID to your Flight class
        private String depart;
        private String arrivee;
        private LocalDate dateDepart;
        private String heureDepart;
        private String heureArrivee;
        private String classe;

        /**
         * Constructs a Vol object with the specified flight details.
         * 
         * @param id           the flight ID.
         * @param depart       the departure city.
         * @param arrivee      the arrival city.
         * @param dateDepart   the departure date.
         * @param heureDepart  the departure time.
         * @param heureArrivee the arrival time.
         * @param classe       the class of the flight (e.g., Economy, Business).
         */
         public Vol(int id, String depart, String arrivee, LocalDate dateDepart, String heureDepart, String heureArrivee, String classe) {
             this.id = id;
             this.depart = depart;
             this.arrivee = arrivee;
             this.dateDepart = dateDepart;
             this.heureDepart = heureDepart;
             this.heureArrivee = heureArrivee;
             this.classe = classe;
         }

        // Getters for all fields

        public int getId() {
            return id;
        }

        public String getDepart() {
            return depart;
        }

        public String getArrivee() {
            return arrivee;
        }

        public LocalDate getDateDepart() {
            return dateDepart;
        }

        public String getHeureDepart() {
            return heureDepart;
        }

        public String getHeureArrivee() {
            return heureArrivee;
        }

        public String getClasse() {
            return classe;
        }
    }

    /**
     * Main method to demonstrate the use of the AffichageVol class.
     * 
     * @param args command line arguments (not used here).
     */
    public static void main(String[] args) {
        // Example usage (In your main application, replace this with your actual Vol object)
        // LocalDate dateDepart = LocalDate.of(2024, 2, 15);
        // Vol vol = new AffichageVol.Vol(1, "Saint-Louis", "Barcelone", dateDepart, "11h00", "12h45", "Economique");

        // SwingUtilities.invokeLater(() -> new AffichageVol(vol));
    }
}
