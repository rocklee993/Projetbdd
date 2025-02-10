import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VolGUI extends JFrame {
    private JTextField idField, dateDepartField, dateArriveeField, lieuDepartField, lieuArriveeField;
    private JTextArea outputArea;
    private VolDAO volDAO; // DAO for database operations

    public VolGUI() {
        volDAO = new VolDAO(); // Initialize DAO

        setTitle("Gestion des Vols");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.CYAN);
        JLabel titleLabel = new JLabel("Gestion des Vols");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel);

        // Input Fields Panel
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("ID du vol:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Date Départ (yyyy-MM-dd HH:mm):"));
        dateDepartField = new JTextField();
        panel.add(dateDepartField);

        panel.add(new JLabel("Date Arrivée (yyyy-MM-dd HH:mm):"));
        dateArriveeField = new JTextField();
        panel.add(dateArriveeField);

        panel.add(new JLabel("Lieu Départ:"));
        lieuDepartField = new JTextField();
        panel.add(lieuDepartField);

        panel.add(new JLabel("Lieu Arrivée:"));
        lieuArriveeField = new JTextField();
        panel.add(lieuArriveeField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");

        addButton.setBackground(new Color(66, 135, 245));
        addButton.setForeground(Color.WHITE);
        editButton.setBackground(new Color(255, 165, 0));
        editButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Output Area
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Add Components to Frame
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> createVol());
        editButton.addActionListener(e -> modifyVol());
        deleteButton.addActionListener(e -> deleteVol());

        refreshVolsList(); // Display existing flights on startup

        setVisible(true);
    }

    private void createVol() {
        try {
            int id = Integer.parseInt(idField.getText());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateDepart = LocalDateTime.parse(dateDepartField.getText(), formatter);
            LocalDateTime dateArrivee = LocalDateTime.parse(dateArriveeField.getText(), formatter);
            String lieuDepart = lieuDepartField.getText();
            String lieuArrivee = lieuArriveeField.getText();

            Vol vol = new Vol(id, dateDepart, dateArrivee, lieuDepart, lieuArrivee);
            volDAO.ajouterVol(vol); // Add to database

            outputArea.setText("✅ Vol ajouté avec succès !");
            refreshVolsList();
            clearFields();
        } catch (Exception ex) {
            outputArea.setText("❌ Erreur: Vérifiez vos entrées.");
        }
    }

    private void modifyVol() {
        try {
            int id = Integer.parseInt(idField.getText().trim()); // Get the ID

            // If fields are empty, retrieve the flight from the database
            if (lieuDepartField.getText().isEmpty() &&
                lieuArriveeField.getText().isEmpty() &&
                dateDepartField.getText().isEmpty() &&
                dateArriveeField.getText().isEmpty()) {

                Vol vol = volDAO.getVol(id); // Fetch from DB

                if (vol != null) {
                    // Fill input fields with retrieved values
                    lieuDepartField.setText(vol.getLieuDepart());
                    lieuArriveeField.setText(vol.getLieuArrivee());
                    dateDepartField.setText(vol.getDateDepart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    dateArriveeField.setText(vol.getDateArrivee().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    outputArea.setText("✏️ Vol chargé, vous pouvez maintenant modifier les valeurs.");
                } else {
                    outputArea.setText("⚠️ Aucun vol trouvé avec cet ID.");
                }

            } else {
                // If fields are already filled, update the flight
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime dateDepart = LocalDateTime.parse(dateDepartField.getText(), formatter);
                LocalDateTime dateArrivee = LocalDateTime.parse(dateArriveeField.getText(), formatter);
                String lieuDepart = lieuDepartField.getText();
                String lieuArrivee = lieuArriveeField.getText();

                Vol vol = new Vol(id, dateDepart, dateArrivee, lieuDepart, lieuArrivee);
                volDAO.modifierVol(vol); // Update in database

                outputArea.setText("✅ Vol modifié avec succès !");
                refreshVolsList();
            }
        } catch (NumberFormatException ex) {
            outputArea.setText("❌ Erreur: L'ID doit être un nombre valide.");
        } catch (Exception ex) {
            outputArea.setText("❌ Erreur: Vérifiez vos entrées.");
        }
    }


    private void deleteVol() {
        try {
            int id = Integer.parseInt(idField.getText());

            volDAO.supprimerVol(id); // Delete from database
            outputArea.setText("✅ Vol supprimé avec succès !");
            refreshVolsList();
            clearFields();
        } catch (Exception ex) {
            outputArea.setText("❌ Erreur: Vérifiez l'ID.");
        }
    }

    private void refreshVolsList() {
        List<Vol> vols = volDAO.getTousLesVols();
        outputArea.setText("📋 Liste des vols:\n");

        if (vols.isEmpty()) {
            outputArea.append("Aucun vol enregistré.\n");
        } else {
            for (Vol vol : vols) {
                outputArea.append(vol + "\n");
            }
        }
    }

    private void clearFields() {
        idField.setText("");
        dateDepartField.setText("");
        dateArriveeField.setText("");
        lieuDepartField.setText("");
        lieuArriveeField.setText("");
    }

    public static void main(String[] args) {
        new VolGUI();
    }
}
