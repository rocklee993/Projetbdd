import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VolGUI extends JFrame {
    private JTextField idField, dateDepartField, dateArriveeField, lieuDepartField, lieuArriveeField;
    private JTextArea outputArea;
    private VolDAO volDAO;
    private Color primaryColor = new Color(0, 206, 209); // Turquoise

    public VolGUI() {
        volDAO = new VolDAO();
        setTitle("Gestion des Vols");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Navigation Bar
        createNavBar();

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Input Panel
        createInputPanel(mainPanel);

        // Output Area with new styling
        createOutputArea(mainPanel);

        add(mainPanel, BorderLayout.CENTER);
        refreshVolsList();
        setVisible(true);
    }

    private void createNavBar() {
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navBar.setBackground(primaryColor);

        String[] menuItems = {"Gestion des Vols", "Afficher tous les vols", "Déconnexion"};
        for (String item : menuItems) {
            JButton btn = createNavButton(item);
            if (item.equals("Afficher tous les vols")) {
                btn.setBackground(primaryColor.darker());
            }
            navBar.add(btn);

            // Action listeners
            if (item.equals("Déconnexion")) {
                btn.addActionListener(e -> logoutAction());
            } else if (item.equals("Afficher tous les vols")) {
                btn.addActionListener(e -> openVolGUI());
            }
        }

        add(navBar, BorderLayout.NORTH);
    }

    private void logoutAction() {
        // Logic to handle logout
        // For example, you could dispose of the current window and show the login screen.
        this.dispose(); // Close the current window
        new LoginGUI(); // Assuming LoginGUI is the class for your login screen
    }

    private void openVolGUI() {
        // Logic to open VolGUI
        // For example, you could dispose of the current window and show the VolGUI screen.
        this.dispose(); // Close the current window
        new AfficherVolGUI(); // Assuming VolGUI is the class for your VolGUI
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(primaryColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(primaryColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(primaryColor);
            }
        });

        return button;
    }

    private void createInputPanel(JPanel mainPanel) {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create styled text fields
        idField = createStyledTextField();
        dateDepartField = createStyledTextField();
        dateArriveeField = createStyledTextField();
        lieuDepartField = createStyledTextField();
        lieuArriveeField = createStyledTextField();

        // Add fields to panel
        addLabelAndField(inputPanel, gbc, "ID du vol", idField, 0);
        addLabelAndField(inputPanel, gbc, "Date Départ (yyyy-MM-dd HH:mm)", dateDepartField, 1);
        addLabelAndField(inputPanel, gbc, "Date Arrivée (yyyy-MM-dd HH:mm)", dateArriveeField, 2);
        addLabelAndField(inputPanel, gbc, "Lieu Départ", lieuDepartField, 3);
        addLabelAndField(inputPanel, gbc, "Lieu Arrivée", lieuArriveeField, 4);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton("Ajouter", new Color(66, 135, 245));
        JButton editButton = createStyledButton("Modifier", new Color(255, 165, 0));
        JButton deleteButton = createStyledButton("Supprimer", new Color(220, 20, 60));

        addButton.addActionListener(e -> createVol());
        editButton.addActionListener(e -> modifyVol());
        deleteButton.addActionListener(e -> deleteVol());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
    }

    private void createOutputArea(JPanel mainPanel) {
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 12));
        outputArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 30));
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 30));
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, 
                                JComponent field, int position) {
        gbc.gridx = position % 2;
        gbc.gridy = position / 2 * 2;
        gbc.gridwidth = 1;
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(label, gbc);

        gbc.gridy++;
        panel.add(field, gbc);
    }

    // Keep the original logic for these methods
   

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
