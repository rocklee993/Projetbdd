import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AffichageVol extends JFrame {

    private JTextArea volInfoArea;
    private JButton annulerButton;
    private Vol volAAfficher;

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



    // Inner class Vol (You'll need this, possibly modified, from your other classes)
     class Vol {
        private int id; // Add an ID to your Flight class
        private String depart;
        private String arrivee;
        private LocalDate dateDepart;
        private String heureDepart;
        private String heureArrivee;
        private String classe;


         public Vol(int id, String depart, String arrivee, LocalDate dateDepart, String heureDepart, String heureArrivee, String classe)
         {

             this.id = id;
             this.depart = depart;
             this.arrivee = arrivee;
             this.dateDepart = dateDepart;
             this.heureDepart = heureDepart;
             this.heureArrivee = heureArrivee;
             this.classe = classe;
         }



        // ... (Getters for all fields)


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

    public static void main(String[] args) {

          // Example usage (In your main application, replace this with your actual Vol object)
        //LocalDate dateDepart = LocalDate.of(2024, 2, 15);
       //Vol vol = new AffichageVol.Vol(1, "Saint-Louis", "Barcelone", dateDepart, "11h00", "12h45", "Economique");


       //SwingUtilities.invokeLater(() -> new AffichageVol(vol));

    }
}