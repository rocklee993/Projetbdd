

import javax.swing.*;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class VolGUI extends JFrame {
    private JTextField idField, lieuDepartField, lieuArriveeField;
    private JTextArea outputArea;
    private VolDAO volDAO;
    private Color primaryColor = new Color(0, 206, 209); // Turquoise

    private JDatePickerImpl dateDepartPicker, dateArriveePicker; // Ajout des sélecteurs de date
    private TimePicker timeDepartPicker, timeArriveePicker; // Ajout des sélecteurs d'heure

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
        lieuDepartField = createStyledTextField();
        lieuArriveeField = createStyledTextField();

        // Création des sélecteurs de date
        UtilDateModel modelDepart = new UtilDateModel();
        Properties pDepart = new Properties();
        pDepart.put("text.today", "Aujourd'hui");
        pDepart.put("text.month", "Mois");
        pDepart.put("text.year", "Année");
        JDatePanelImpl datePanelDepart = new JDatePanelImpl(modelDepart, pDepart);
        dateDepartPicker = new JDatePickerImpl(datePanelDepart, new DateLabelFormatter());

        UtilDateModel modelArrivee = new UtilDateModel();
        Properties pArrivee = new Properties();
        pArrivee.put("text.today", "Aujourd'hui");
        pArrivee.put("text.month", "Mois");
        pArrivee.put("text.year", "Année");
        JDatePanelImpl datePanelArrivee = new JDatePanelImpl(modelArrivee, pArrivee);
        dateArriveePicker = new JDatePickerImpl(datePanelArrivee, new DateLabelFormatter());

        // Création des sélecteurs d'heure
        timeDepartPicker = new TimePicker();
        timeArriveePicker = new TimePicker();

        // Add fields to panel
        addLabelAndField(inputPanel, gbc, "ID du vol", idField, 0);
        addLabelAndField(inputPanel, gbc, "Date Départ", (JComponent) dateDepartPicker, 1); // Ajout du datePicker
        addLabelAndField(inputPanel, gbc, "Heure Départ", timeDepartPicker, 2); // Ajout du sélecteur d'heure
        addLabelAndField(inputPanel, gbc, "Date Arrivée", (JComponent) dateArriveePicker, 3); // Ajout du datePicker
        addLabelAndField(inputPanel, gbc, "Heure Arrivée", timeArriveePicker, 4); // Ajout du sélecteur d'heure
        addLabelAndField(inputPanel, gbc, "Lieu Départ", lieuDepartField, 5);
        addLabelAndField(inputPanel, gbc, "Lieu Arrivée", lieuArriveeField, 6);

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

    private void createVol() {
        try {
            int id = Integer.parseInt(idField.getText());

            // Récupérer la date sélectionnée
            Date selectedDateDepart = (Date) dateDepartPicker.getModel().getValue();
            Date selectedDateArrivee = (Date) dateArriveePicker.getModel().getValue();

            // Récupérer l'heure sélectionnée
            int selectedHourDepart = timeDepartPicker.getSelectedHour();
            int selectedMinuteDepart = timeDepartPicker.getSelectedMinute();
            int selectedHourArrivee = timeArriveePicker.getSelectedHour();
            int selectedMinuteArrivee = timeArriveePicker.getSelectedMinute();

            // Validation des dates
            if (selectedDateDepart == null || selectedDateArrivee == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une date de départ et d'arrivée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Combiner la date et l'heure
            Calendar calDepart = Calendar.getInstance();
            calDepart.setTime(selectedDateDepart);
            calDepart.set(Calendar.HOUR_OF_DAY, selectedHourDepart);
            calDepart.set(Calendar.MINUTE, selectedMinuteDepart);
            calDepart.set(Calendar.SECOND, 0); // Assurez-vous de mettre les secondes à zéro
            Date combinedDateDepart = calDepart.getTime();

            Calendar calArrivee = Calendar.getInstance();
            calArrivee.setTime(selectedDateArrivee);
            calArrivee.set(Calendar.HOUR_OF_DAY, selectedHourArrivee);
            calArrivee.set(Calendar.MINUTE, selectedMinuteArrivee);
            calArrivee.set(Calendar.SECOND, 0); // Assurez-vous de mettre les secondes à zéro
            Date combinedDateArrivee = calArrivee.getTime();

            String lieuDepart = lieuDepartField.getText();
            String lieuArrivee = lieuArriveeField.getText();

            // Formatage des dates pour la base de données (yyyy-MM-dd HH:mm:ss)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateDepart = sdf.format(combinedDateDepart);
            String formattedDateArrivee = sdf.format(combinedDateArrivee);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateDepart = LocalDateTime.parse(formattedDateDepart, formatter);
            LocalDateTime dateArrivee = LocalDateTime.parse(formattedDateArrivee, formatter);

            Vol vol = new Vol(id, dateDepart, dateArrivee, lieuDepart, lieuArrivee);
            volDAO.ajouterVol(vol); // Add to database

            outputArea.setText("✅ Vol ajouté avec succès !");
            refreshVolsList();
            clearFields();
        } catch (NumberFormatException ex) {
            outputArea.setText("❌ Erreur: Vérifiez que l'ID est un nombre valide.");
        } catch (Exception ex) {
            outputArea.setText("❌ Erreur: " + ex.getMessage());
        }
    }


    private void modifyVol() {
                try {
            int id = Integer.parseInt(idField.getText());

            // Récupérer la date sélectionnée
            Date selectedDateDepart = (Date) dateDepartPicker.getModel().getValue();
            Date selectedDateArrivee = (Date) dateArriveePicker.getModel().getValue();

            // Récupérer l'heure sélectionnée
            int selectedHourDepart = timeDepartPicker.getSelectedHour();
            int selectedMinuteDepart = timeDepartPicker.getSelectedMinute();
            int selectedHourArrivee = timeArriveePicker.getSelectedHour();
            int selectedMinuteArrivee = timeArriveePicker.getSelectedMinute();

            // Validation des dates
            if (selectedDateDepart == null || selectedDateArrivee == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une date de départ et d'arrivée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Combiner la date et l'heure
            Calendar calDepart = Calendar.getInstance();
            calDepart.setTime(selectedDateDepart);
            calDepart.set(Calendar.HOUR_OF_DAY, selectedHourDepart);
            calDepart.set(Calendar.MINUTE, selectedMinuteDepart);
            calDepart.set(Calendar.SECOND, 0); // Assurez-vous de mettre les secondes à zéro
            Date combinedDateDepart = calDepart.getTime();

            Calendar calArrivee = Calendar.getInstance();
            calArrivee.setTime(selectedDateArrivee);
            calArrivee.set(Calendar.HOUR_OF_DAY, selectedHourArrivee);
            calArrivee.set(Calendar.MINUTE, selectedMinuteArrivee);
            calArrivee.set(Calendar.SECOND, 0); // Assurez-vous de mettre les secondes à zéro
            Date combinedDateArrivee = calArrivee.getTime();

            String lieuDepart = lieuDepartField.getText();
            String lieuArrivee = lieuArriveeField.getText();

            // Formatage des dates pour la base de données (yyyy-MM-dd HH:mm:ss)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateDepart = sdf.format(combinedDateDepart);
            String formattedDateArrivee = sdf.format(combinedDateArrivee);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateDepart = LocalDateTime.parse(formattedDateDepart, formatter);
            LocalDateTime dateArrivee = LocalDateTime.parse(formattedDateArrivee, formatter);

            Vol vol = new Vol(id, dateDepart, dateArrivee, lieuDepart, lieuArrivee);
            volDAO.modifierVol(vol); // Update in database

            outputArea.setText("✅ Vol Modifié avec succès !");
            refreshVolsList();
            clearFields();
        } catch (NumberFormatException ex) {
            outputArea.setText("❌ Erreur: Vérifiez que l'ID est un nombre valide.");
        } catch (Exception ex) {
            outputArea.setText("❌ Erreur: " + ex.getMessage());
        }
    }

    // Classe interne pour formater la date dans JDatePicker
    private class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }
    }

    private void createNavBar() { // Crée la barre de navigation
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Crée un panneau avec un FlowLayout (alignement à gauche)
        navBar.setBackground(primaryColor); // Définit la couleur de fond de la barre de navigation

        String[] menuItems = {"Gestion des Vols", "Afficher tous les vols", "Aceuil"}; // Les éléments du menu
        for (String item : menuItems) { // Parcours les éléments du menu
            JButton btn = createNavButton(item); // Crée un bouton pour chaque élément
            if (item.equals("Afficher tous les vols")) {
                btn.setBackground(primaryColor.darker());
            }
            navBar.add(btn); // Ajoute le bouton à la barre de navigation

            // Action listeners
            if (item.equals("Aceuil")) {
                btn.addActionListener(e -> Aceuil()); // Ajoute un ActionListener pour la déconnexion
            } else if (item.equals("Afficher tous les vols")) {
                btn.addActionListener(e -> openVolGUI()); // Ajoute un ActionListener pour afficher tous les vols
            }
        }

        add(navBar, BorderLayout.NORTH); // Ajoute la barre de navigation en haut de la fenêtre
    }

    private void Aceuil() { // Méthode pour gérer la déconnexion
        // Logic to handle logout
        // For example, you could dispose of the current window and show the login screen.
        this.dispose(); // Close the current window
        new AccueilGUI(); // Assuming LoginGUI is the class for your login screen
    }

    private void openVolGUI() { // Méthode pour ouvrir la VolGUI
        // Logic to open VolGUI
        // For example, you could dispose of the current window and show the VolGUI screen.
        this.dispose(); // Close the current window
        new AfficherVolGUI(); // Assuming VolGUI is the class for your VolGUI
    }

    private JButton createNavButton(String text) { // Méthode pour créer un bouton pour la barre de navigation
        JButton button = new JButton(text); // Crée un bouton avec le texte spécifié
        button.setPreferredSize(new Dimension(150, 40)); // Définit la taille du bouton
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Définit la police du bouton
        button.setForeground(Color.WHITE); // Définit la couleur du texte du bouton
        button.setBackground(primaryColor); // Définit la couleur de fond du bouton
        button.setBorderPainted(false); // Supprime la bordure du bouton
        button.setFocusPainted(false); // Supprime l'effet de focus du bouton

        button.addMouseListener(new java.awt.event.MouseAdapter() { // Ajoute un MouseListener pour gérer les événements de la souris
            public void mouseEntered(java.awt.event.MouseEvent evt) { // Méthode appelée lorsque la souris entre dans la zone du bouton
                button.setBackground(primaryColor.darker()); // Assombrit la couleur de fond du bouton
            }
            public void mouseExited(java.awt.event.MouseEvent evt) { // Méthode appelée lorsque la souris sort de la zone du bouton
                button.setBackground(primaryColor); // Rétablit la couleur de fond du bouton
            }
        });

        return button; // Retourne le bouton créé
    }

    private void createOutputArea(JPanel mainPanel) { // Crée la zone de sortie
        outputArea = new JTextArea(10, 40); // Crée une zone de texte
        outputArea.setEditable(false); // Empêche l'utilisateur de modifier le contenu de la zone de texte
        outputArea.setFont(new Font("Arial", Font.PLAIN, 12)); // Définit la police de la zone de texte
        outputArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Ajoute une bordure à la zone de texte

        JScrollPane scrollPane = new JScrollPane(outputArea); // Ajoute une barre de défilement à la zone de texte
        scrollPane.setBorder(BorderFactory.createCompoundBorder( // Ajoute une bordure à la barre de défilement
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), // Crée une bordure de ligne
            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Crée une bordure vide
        ));

        mainPanel.add(scrollPane, BorderLayout.CENTER); // Ajoute la barre de défilement au centre du panneau principal
    }

    private JTextField createStyledTextField() { // Crée un champ de texte stylisé
        JTextField field = new JTextField(); // Crée un champ de texte
        field.setPreferredSize(new Dimension(200, 30)); // Définit la taille du champ de texte
        field.setFont(new Font("Arial", Font.PLAIN, 12)); // Définit la police du champ de texte
        return field; // Retourne le champ de texte créé
    }

    private JButton createStyledButton(String text, Color bgColor) { // Crée un bouton stylisé
        JButton button = new JButton(text); // Crée un bouton avec le texte spécifié
        button.setBackground(bgColor); // Définit la couleur de fond du bouton
        button.setForeground(Color.WHITE); // Définit la couleur du texte du bouton
        button.setFont(new Font("Arial", Font.BOLD, 12)); // Définit la police du bouton
        button.setPreferredSize(new Dimension(120, 30)); // Définit la taille du bouton
        button.setBorderPainted(false); // Supprime la bordure du bouton
        button.setFocusPainted(false); // Supprime l'effet de focus du bouton

        button.addMouseListener(new java.awt.event.MouseAdapter() { // Ajoute un MouseListener pour gérer les événements de la souris
            public void mouseEntered(java.awt.event.MouseEvent evt) { // Méthode appelée lorsque la souris entre dans la zone du bouton
                button.setBackground(bgColor.darker()); // Assombrit la couleur de fond du bouton
            }
            public void mouseExited(java.awt.event.MouseEvent evt) { // Méthode appelée lorsque la souris sort de la zone du bouton
                button.setBackground(bgColor); // Rétablit la couleur de fond du bouton
            }
        });

        return button; // Retourne le bouton créé
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText,
                                 JComponent field, int position) { // Ajoute un label et un champ à un panneau
        gbc.gridx = position % 2; // Définit la position du composant dans la grille (colonne)
        gbc.gridy = position / 2 * 2; // Définit la position du composant dans la grille (ligne)
        gbc.gridwidth = 1; // Définit la largeur du composant
        
        JLabel label = new JLabel(labelText); // Crée un label avec le texte spécifié
        label.setFont(new Font("Arial", Font.BOLD, 12)); // Définit la police du label
        panel.add(label, gbc); // Ajoute le label au panneau

        gbc.gridy++; // Incrémente la position verticale
        panel.add(field, gbc); // Ajoute le champ au panneau
    }

    private void deleteVol() { // Méthode pour supprimer un vol
        try { // Tente de supprimer un vol
            int id = Integer.parseInt(idField.getText()); // Récupère l'ID du vol depuis le champ de texte

            volDAO.supprimerVol(id); // Delete from database // Supprime le vol de la base de données
            outputArea.setText("✅ Vol supprimé avec succès !"); // Affiche un message de succès
            refreshVolsList(); // Actualise la liste des vols
            clearFields(); // Efface les champs
        } catch (Exception ex) { // Si une erreur se produit
            outputArea.setText("❌ Erreur: Vérifiez l'ID."); // Affiche un message d'erreur
        }
    }

    private void refreshVolsList() { // Méthode pour actualiser la liste des vols
        List<Vol> vols = volDAO.getTousLesVols(); // Récupère tous les vols depuis la base de données
        outputArea.setText("📋 Liste des vols:\n"); // Définit le texte de la zone de sortie

        if (vols.isEmpty()) { // Si aucun vol n'est enregistré
            outputArea.append("Aucun vol enregistré.\n"); // Affiche un message
        } else { // Sinon
            for (Vol vol : vols) { // Parcours les vols
                outputArea.append(vol + "\n"); // Ajoute les informations du vol à la zone de sortie
            }
        }
    }

    private void clearFields() { // Méthode pour effacer les champs
        idField.setText(""); // Efface le champ ID
        lieuDepartField.setText(""); // Efface le champ Lieu de départ
        lieuArriveeField.setText(""); // Efface le champ Lieu d'arrivée
    }

    public static void main(String[] args) { // Méthode principale
    	  new VolGUI(); // Crée une nouvelle instance de VolGUI
    }
}