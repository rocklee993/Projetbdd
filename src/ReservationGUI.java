import javax.swing.*;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class ReservationGUI extends JFrame { // class is here

    private JTextField departField, arriveeField, dateField;
    private JTable volsTable;
    private DefaultTableModel tableModel;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Color primaryColor = new Color(0, 206, 209);
    private static final String USER_HOME = System.getProperty("user.home");
    private LoginGUI loginPage;

    private JTable reservationsTable; // Tableau pour afficher les réservations
    private DefaultTableModel reservationsTableModel; // Modèle pour le tableau des réservations
    private JPanel mainPanel; // Déclarez mainPanel comme variable de classe
    private JPanel reservationsPanel;

    public ReservationGUI() {
        setTitle("Réserver un Vol");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createNavBar();
        mainPanel = new JPanel(new BorderLayout(10, 10)); // Correction: utilise la variable de classe
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        createSearchPanel(mainPanel);
        createResultTable(mainPanel);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void createNavBar() {
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navBar.setBackground(primaryColor);

        String[] menuItems = {"Voir réservation", "Connexion"};
        for (String item : menuItems) {
            JButton btn = createNavButton(item);
            navBar.add(btn);
            if (item.equals("Connexion")) {
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Méthode appelée lorsque le bouton "Connexion" est cliqué
                        openLoginPage();
                    }
                });
            } else if (item.equals("Voir réservation")) {
                btn.addActionListener(e -> showUserReservations());
            }
        }

        add(navBar, BorderLayout.NORTH);
    }

    private void showUserReservations() {
        if (Session.userId == -1) {
            JOptionPane.showMessageDialog(this, "Vous devez être connecté pour voir vos réservations.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Supprimer le contenu actuel du mainPanel
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();

        // Créer un tableau pour afficher les réservations
        reservationsTableModel = new DefaultTableModel(
            new Object[]{"ID Réservation", "Départ", "Arrivée", "Date Départ", "Date Arrivée", "Statut"}, 0
        );
        reservationsTable = new JTable(reservationsTableModel);
        JScrollPane scrollPane = new JScrollPane(reservationsTable);

        // Charger les réservations de l'utilisateur
        loadUserReservations();

        // Créer un bouton pour revenir à la recherche de vols
        JButton backButton = new JButton("Retour à la recherche de vols");
        styleButton(backButton);
        backButton.addActionListener(e -> showFlightSearch());

        // Ajouter le tableau et le bouton à un nouveau panneau
        reservationsPanel = new JPanel(new BorderLayout());  // Initialisation de reservationsPanel
        reservationsPanel.add(scrollPane, BorderLayout.CENTER);
        reservationsPanel.add(backButton, BorderLayout.SOUTH);

        // Ajouter le panneau au mainPanel
        mainPanel.add(reservationsPanel, BorderLayout.CENTER);

        // Rafraîchir l'affichage
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void loadUserReservations() {
        List<Reservation> reservations = ReservationDAO.getReservationsByUserId(Session.userId);

        System.out.println("Nombre de réservations récupérées : " + reservations.size()); // Ajout pour débogage

        for (Reservation reservation : reservations) {
            reservationsTableModel.addRow(new Object[]{
                reservation.getId(),
                reservation.getLieuDepart(),
                reservation.getLieuArrivee(),
                reservation.getDateDepart(),
                reservation.getDateArrivee(),
                reservation.getStatus()
            });
        }
    }


    private void showFlightSearch() {
        // Supprimer le contenu actuel du mainPanel
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();

        // Recréer et ajouter les panneaux de recherche et de résultats
        createSearchPanel(mainPanel);
        createResultTable(mainPanel);

        // Rafraîchir l'affichage
        mainPanel.revalidate();
        mainPanel.repaint();
    }


    private void openLoginPage() {
        // Vérifiez si la page de login existe déjà
        if (loginPage == null) {
            loginPage = new LoginGUI(); // Utilise le constructeur par défaut de LoginGUI
        }
        loginPage.setVisible(true); // Affiche la page de login
        this.dispose(); // Ferme la fenêtre actuelle (ReservationGUI)
    }


    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        // Use java.awt.Font here
        button.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
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

    private void createSearchPanel(JPanel mainPanel) {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(java.awt.Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        departField = createStyledTextField();
        arriveeField = createStyledTextField();
        dateField = createStyledTextField();

        addLabelAndField(searchPanel, gbc, "Départ", departField, 0);
        addLabelAndField(searchPanel, gbc, "Arrivée", arriveeField, 1);
        addLabelAndField(searchPanel, gbc, "Date (YYYY-MM-DD)", dateField, 2);

        JButton searchButton = new JButton("Rechercher");
        styleButton(searchButton);
        searchButton.addActionListener(e -> rechercherVols());

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        searchPanel.add(searchButton, gbc);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
    }

    private void createResultTable(JPanel mainPanel) {
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Départ", "Arrivée", "Date Départ", "Date Arrivée", "Durée", "Réserver"},
                0
        );
        volsTable = new JTable(tableModel);

        volsTable.setRowHeight(35);
        // Use java.awt.Font here
        volsTable.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        volsTable.getTableHeader().setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));

        volsTable.getColumn("Réserver").setCellRenderer(new ButtonRenderer());
        volsTable.getColumn("Réserver").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(volsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 30));
        return field;
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText,
                                 JComponent field, int position) {
        gbc.gridx = position % 2;
        gbc.gridy = position / 2 * 2;

        JLabel label = new JLabel(labelText);
        label.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        panel.add(label, gbc);

        gbc.gridy++;
        panel.add(field, gbc);
    }

    private void styleButton(JButton button) {
        button.setBackground(primaryColor);
        button.setForeground(Color.WHITE);
        button.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 30));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
    }

    private void rechercherVols() {
        String depart = departField.getText();
        String arrivee = arriveeField.getText();
        String date = dateField.getText();

        List<Vol> vols = ReservationDAO.rechercherVols(depart, arrivee, date);
        tableModel.setRowCount(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Vol vol : vols) {
            tableModel.addRow(new Object[]{
                    vol.getId(),
                    vol.getLieuDepart(),
                    vol.getLieuArrivee(),
                    vol.getDateDepart().format(formatter),
                    vol.getDateArrivee().format(formatter),
                    vol.getDureeVol() + " min",
                    "Réserver"
            });
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Réserver");
            styleButton(this);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int selectedRow;
        private int flightId;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Réserver");
            styleButton(button);
            button.addActionListener(e -> {
                selectedRow = volsTable.getSelectedRow();
                if (selectedRow != -1) {
                    flightId = (int) tableModel.getValueAt(selectedRow, 0);
                    reserverVol(flightId);
                } else {
                    JOptionPane.showMessageDialog(ReservationGUI.this, "Veuillez sélectionner un vol.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Réserver";
        }
    }

    private void reserverVol(int flightId) {
        if (Session.userId == -1) {
            JOptionPane.showMessageDialog(this, "Vous devez être connecté pour réserver.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Confirmez-vous la réservation ?", "Confirmation",
                JOptionPane.OK_CANCEL_OPTION);

        if (confirmation == JOptionPane.OK_OPTION) {
            boolean success = ReservationDAO.createReservation(flightId);
            if (success) {
                JPanel messagePane = new JPanel();
                messagePane.setLayout(new BoxLayout(messagePane, BoxLayout.Y_AXIS));
                messagePane.add(new JLabel("Réservation réussie !"));

                JButton downloadButton = new JButton("Télécharger PDF");
                styleButton(downloadButton);
                downloadButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                downloadButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            generateReservationPDF(flightId);
                        } catch (DocumentException | IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(ReservationGUI.this,
                                    "Erreur lors de la création du PDF.", "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                messagePane.add(downloadButton);

                JOptionPane.showMessageDialog(this, messagePane, "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la réservation.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void generateReservationPDF(int flightId) throws DocumentException, IOException {

        Vol vol = ReservationDAO.getVolDetails(flightId);

        if (vol == null) {
            JOptionPane.showMessageDialog(this, "Détails du vol introuvables.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fileName = "Reservation_" + flightId + ".pdf";
        String filePath = USER_HOME + File.separator + "Downloads" + File.separator + fileName;

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // *** PDF DESIGN START ***

        // 1. Metadata
        document.addTitle("Flight Reservation Details");
        document.addSubject("Flight Reservation");
        document.addKeywords("flight, reservation, ticket");
        document.addAuthor("Your Company Name"); // Replace with your own

        // 2. Fonts and Colors
        BaseColor primaryColor = new BaseColor(0, 150, 136); // Teal (plus foncé et plus moderne)
        BaseColor secondaryColor = new BaseColor(255, 255, 255); // Blanc
        BaseColor textColor = new BaseColor(51, 51, 51); // Gris foncé
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, primaryColor); // Titre plus grand
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, secondaryColor); // En-têtes blancs sur fond coloré
        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12, textColor);
        Font smallItalic = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, textColor); // Pour le pied de page

        // 3. Logo (replace "path/to/your/logo.png" with the actual path)
        try {
            String imagePath = "C:\\Users\\badr4\\OneDrive\\Bureau\\hhh.jpg"; // put your path here
            File imageFile = new File(imagePath);
            if (imageFile.exists()) { // Check if file exists
                Image logo = Image.getInstance(imagePath);

                logo.scaleToFit(100, 100); // Adjust size as needed
                logo.setAlignment(Element.ALIGN_LEFT); // Align the logo

                document.add(logo);
            }
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }

        // 4. Title
        Paragraph title = new Paragraph("Flight Reservation Details", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(20);
        document.add(title);

        // 5. Reservation Info Table
        PdfPTable table = new PdfPTable(2); // 2 columns
        table.setWidthPercentage(80); // Table takes up 80% of the page width (plus esthétique)
        table.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrer la table
        table.setSpacingBefore(20);
        table.getDefaultCell().setBorder(0); // Supprimer les bordures par défaut des cellules

        // Configuration des couleurs pour les cellules
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(primaryColor);
        headerCell.setPadding(5);

        PdfPCell contentCell = new PdfPCell();
        contentCell.setPadding(5);
        contentCell.setBorder(0); // Enlever les bordures des cellules de contenu

        //Add headers to table
        addTableHeader(table, headerFont, headerCell);

        //Add cells to table
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        //On n'affiche plus l'ID du vol
        addTableCell(table, "Departure", vol.getLieuDepart(), contentFont, contentCell);
        addTableCell(table, "Arrival", vol.getLieuArrivee(), contentFont, contentCell);
        addTableCell(table, "Departure Date", vol.getDateDepart().format(formatter), contentFont, contentCell);
        addTableCell(table, "Arrival Date", vol.getDateArrivee().format(formatter), contentFont, contentCell);
        addTableCell(table, "Flight Duration", vol.getDureeVol() + " min", contentFont, contentCell);

        document.add(table);

        // 6. Footer
        Paragraph footer = new Paragraph("Thank you for flying with us!", smallItalic); // Police plus petite et italique
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(50);
        document.add(footer);

        // ***PDF DESIGN END***

        document.close();

        JOptionPane.showMessageDialog(this, "PDF de réservation créé avec succès dans le dossier Téléchargements.",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    // Helper method to add table header
    private void addTableHeader(PdfPTable table, Font font, PdfPCell headerCell) {

        headerCell.setPhrase(new Phrase("Detail", font));
        table.addCell(headerCell);

        headerCell.setPhrase(new Phrase("Value", font));
        table.addCell(headerCell);
    }

    // Helper method to add a table cell
    private void addTableCell(PdfPTable table, String header, String value, Font font, PdfPCell cell) {

        cell.setPhrase(new Phrase(header, font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(value, font));
        table.addCell(cell);
    }

    public static void main(String[] args) {
       Session.userId = 1; //Temporaire pour le test
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ReservationGUI();
        });
    }
}