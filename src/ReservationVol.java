import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ReservationVol extends JFrame implements ActionListener {

    private JTextField departField, arriveeField, dateDepartField, dateArriveeField;
    private JComboBox<String> classeCombo;
    private JRadioButton allerSimpleButton, allerRetourButton;
    private JButton rechercherButton, reserverButton;
    private JTextArea resultArea;

    private List<ReservationVol> vols = new ArrayList<>(); // Placeholder for flight data

    public ReservationVol() {
        setTitle("Réservation de Vol");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new FlowLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 5));

        departField = new JTextField();
        arriveeField = new JTextField();
        dateDepartField = new JTextField();
        dateArriveeField = new JTextField();
        classeCombo = new JComboBox<>(new String[]{"Economique", "Affaire", "Première"});
        allerSimpleButton = new JRadioButton("Aller simple");
        allerRetourButton = new JRadioButton("Aller/retour");
        ButtonGroup group = new ButtonGroup();
        group.add(allerSimpleButton);
        group.add(allerRetourButton);

        panel.add(new JLabel("Départ:"));
        panel.add(departField);
        panel.add(new JLabel("Arrivée:"));
        panel.add(arriveeField);
        panel.add(new JLabel("Date départ:"));
        panel.add(dateDepartField);
        panel.add(new JLabel("Date arrivée:"));
        panel.add(dateArriveeField);



        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        panel2.add(new JLabel("Classe:"));
        panel2.add(classeCombo);
        panel2.add(allerSimpleButton);
        panel2.add(allerRetourButton);

        rechercherButton = new JButton("Rechercher");
        rechercherButton.addActionListener(this);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);


        add(panel);
        add(panel2);
        add(rechercherButton);
        add(scrollPane);


        setVisible(true);

        // Dummy flight data (replace with actual data retrieval)
       
    }


    @Override
    public void actionPerformed(ActionEvent e) {
         if (e.getSource() == rechercherButton) {
            rechercherVols();
        }  else if (e.getSource() instanceof JButton) {  // Check if it's a "Réserver" button
                reserverVol(((JButton) e.getSource()).getName()); 
            }
    }

    private void rechercherVols() {
        resultArea.setText(""); // Clear previous results

        String depart = departField.getText();
        String arrivee = arriveeField.getText();
        LocalDate dateDepart = parseDate(dateDepartField.getText());
        String classe = (String) classeCombo.getSelectedItem();


        if (depart.isEmpty() || arrivee.isEmpty() || dateDepart == null ) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }


         
    }

    private LocalDate parseDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }


    private void reserverVol(String volIndex)
     {
        int index = Integer.parseInt(volIndex);
        ReservationVol vol = vols.get(index);
        // In a real application, you would perform the booking logic here (e.g., update database, generate confirmation, etc.)
        JOptionPane.showMessageDialog(this, "Vol réservé !\n" + vol.toString(), "Confirmation", JOptionPane.INFORMATION_MESSAGE);


    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReservationVol());
    }




    // Inner class to represent a flight
    class Vol {
        private String depart;
        private String arrivee;
        private LocalDate dateDepart;
        private String heureDepart;
        private String heureArrivee;
        private String classe;


        public Vol(String depart, String arrivee, LocalDate dateDepart, String heureDepart, String heureArrivee, String classe) {

            this.depart = depart;
            this.arrivee = arrivee;
            this.dateDepart = dateDepart;
            this.heureDepart = heureDepart;
            this.heureArrivee = heureArrivee;
            this.classe = classe;
        }

        // Getters (you'll need these for your search logic)
         public String getDepart() {
            return depart;
        }

        public String getArrivee() {
             return arrivee;
        }

         public LocalDate getDateDepart() {
             return dateDepart;
        }

        public String getClasse() {
             return classe;
         }




        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return "Départ: " + depart + "\n" +
                    "Direction " + arrivee + "\n" +
                    "Date de départ: " + dateDepart.format(formatter) + "\n" +
                    "Départ: " + heureDepart + "\n" +
                    "Arrivée : " + heureArrivee + "\n" +
                    "Classe : " + classe;
        }
    }
}