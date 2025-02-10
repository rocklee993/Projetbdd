import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VolGUI extends JFrame {
    private JTextField idField, dateDepartField, dateArriveeField, lieuDepartField, lieuArriveeField;
    private JTextArea outputArea;
    private Vol currentVol; // Store the currently selected or created Vol

    public VolGUI() {
        setTitle("Gestion des Vols");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.CYAN);
        JLabel titleLabel = new JLabel("Gestion des Vols");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel);

        // Main Panel with Grey Background
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Creating Labels and Input Fields
        panel.add(new JLabel("ID du vol:"));
        idField = new JTextField();
        idField.setBackground(Color.WHITE);
        panel.add(idField);

        panel.add(new JLabel("Date Départ (yyyy-MM-dd HH:mm):"));
        dateDepartField = new JTextField();
        dateDepartField.setBackground(Color.WHITE);
        panel.add(dateDepartField);

        panel.add(new JLabel("Date Arrivée (yyyy-MM-dd HH:mm):"));
        dateArriveeField = new JTextField();
        dateArriveeField.setBackground(Color.WHITE);
        panel.add(dateArriveeField);

        panel.add(new JLabel("Lieu Départ:"));
        lieuDepartField = new JTextField();
        lieuDepartField.setBackground(Color.WHITE);
        panel.add(lieuDepartField);

        panel.add(new JLabel("Lieu Arrivée:"));
        lieuArriveeField = new JTextField();
        lieuArriveeField.setBackground(Color.WHITE);
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
        outputArea = new JTextArea(3, 30);
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

            currentVol = new Vol(id, dateDepart, dateArrivee, lieuDepart, lieuArrivee); // Set the current Vol
            outputArea.setText("Vol ajouté:\n" + currentVol);
        } catch (Exception ex) {
            outputArea.setText("Erreur: Vérifiez vos entrées.");
        }
    }

    private void modifyVol() {
        if (currentVol != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime dateDepart = LocalDateTime.parse(dateDepartField.getText(), formatter);
                LocalDateTime dateArrivee = LocalDateTime.parse(dateArriveeField.getText(), formatter);
                String lieuDepart = lieuDepartField.getText();
                String lieuArrivee = lieuArriveeField.getText();

                currentVol.setDateDepart(dateDepart);
                currentVol.setDateArrivee(dateArrivee);
                currentVol.setLieuDepart(lieuDepart);
                currentVol.setLieuArrivee(lieuArrivee);
                
                outputArea.setText("Vol modifié:\n" + currentVol);
            } catch (Exception ex) {
                outputArea.setText("Erreur: Vérifiez vos entrées.");
            }
        } else {
            outputArea.setText("Aucun vol à modifier.");
        }
    }

    private void deleteVol() {
        if (currentVol != null) {
            currentVol = null;
            outputArea.setText("Vol supprimé.");
            clearFields();
        } else {
            outputArea.setText("Aucun vol à supprimer.");
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
