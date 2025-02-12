import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn; // Import TableColumn

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

public class ReservationGUI extends JFrame {

    private JTextField departField, arriveeField;
    private JTable volsTable;
    private DefaultTableModel tableModel;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Color primaryColor = new Color(0, 206, 209);
    private static final String USER_HOME = System.getProperty("user.home");
    private LoginGUI loginPage;

    private JTable reservationsTable;
    private DefaultTableModel reservationsTableModel;
    private JPanel mainPanel;
    private JPanel reservationsPanel;

    private JDatePickerImpl datePicker; // Le sélecteur de date

    public ReservationGUI() {
        setTitle("Réserver un Vol");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createNavBar();
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        createSearchPanel(mainPanel);
        createResultTable(mainPanel);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
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

        // Création du sélecteur de date
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Aujourd'hui");
        p.put("text.month", "Mois");
        p.put("text.year", "Année");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        addLabelAndField(searchPanel, gbc, "Départ", departField, 0);
        addLabelAndField(searchPanel, gbc, "Arrivée", arriveeField, 1);
        addLabelAndField(searchPanel, gbc, "Date Départ", (JComponent) datePicker, 2);

        JButton searchButton = new JButton("Rechercher");
        styleButton(searchButton);
        searchButton.addActionListener(e -> rechercherVols());

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        searchPanel.add(searchButton, gbc);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
    }

    private void rechercherVols() {
        String depart = departField.getText();
        String arrivee = arriveeField.getText();

        // Récupération de la date sélectionnée
        Date selectedDate = (Date) datePicker.getModel().getValue();

        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une date.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Formatage de la date pour la requête SQL (yyyy-MM-dd)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(selectedDate);

        List<Vol> vols = ReservationDAO.rechercherVols(depart, arrivee, formattedDate); // Passer la date formatée
        tableModel.setRowCount(0);

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

    private void createNavBar() {
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navBar.setBackground(primaryColor);

        String[] menuItems = {"Voir réservation", "Connexion"};
        for (String item : menuItems) {
            JButton btn = createNavButton(item);
            navBar.add(btn);
            if (item.equals("Connexion")) {
                btn.addActionListener(e -> openLoginPage());
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
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
        reservationsTableModel = new DefaultTableModel(
            new Object[]{"ID Réservation", "Départ", "Arrivée", "Date Départ", "Date Arrivée", "Statut"}, 0
        );
        reservationsTable = new JTable(reservationsTableModel);
        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        loadUserReservations();
        JButton backButton = new JButton("Retour à la recherche de vols");
        styleButton(backButton);
        backButton.addActionListener(e -> showFlightSearch());
        reservationsPanel = new JPanel(new BorderLayout());
        reservationsPanel.add(scrollPane, BorderLayout.CENTER);
        reservationsPanel.add(backButton, BorderLayout.SOUTH);
        mainPanel.add(reservationsPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void loadUserReservations() {
        List<Reservation> reservations = ReservationDAO.getReservationsByUserId(Session.userId);
        System.out.println("Nombre de réservations récupérées : " + reservations.size());
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
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
        createSearchPanel(mainPanel);
        createResultTable(mainPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void openLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginGUI();
        }
        loginPage.setVisible(true);
        this.dispose();
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
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

    private void createResultTable(JPanel mainPanel) {
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Départ", "Arrivée", "Date Départ", "Date Arrivée", "Durée", "Réserver"}, 0
        );
        volsTable = new JTable(tableModel);

        volsTable.setRowHeight(35);
        volsTable.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        volsTable.getTableHeader().setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));

        // Correction: Utilise getColumnModel() et getColumn() pour spécifier la colonne
        TableColumn reserveColumn = volsTable.getColumnModel().getColumn(6);
        reserveColumn.setCellRenderer(new ButtonRenderer());
        reserveColumn.setCellEditor(new ButtonEditor(new JCheckBox()));

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
        document.addTitle("Flight Reservation Details");
        document.addSubject("Flight Reservation");
        document.addKeywords("flight, reservation, ticket");
        document.addAuthor("Your Company Name");

        BaseColor primaryColor = new BaseColor(0, 150, 136);
        BaseColor secondaryColor = new BaseColor(255, 255, 255);
        BaseColor textColor = new BaseColor(51, 51, 51);
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, primaryColor);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, secondaryColor);
        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12, textColor);
        Font smallItalic = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, textColor);

        try {
            String imagePath = "C:\\Users\\badr4\\OneDrive\\Bureau\\hhh.jpg";
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Image logo = Image.getInstance(imagePath);
                logo.scaleToFit(100, 100);
                logo.setAlignment(Element.ALIGN_LEFT);
                document.add(logo);
            }
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }

        Paragraph title = new Paragraph("Flight Reservation Details", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(20);
        document.add(title);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(80);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setSpacingBefore(20);
        table.getDefaultCell().setBorder(0);

        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(primaryColor);
        headerCell.setPadding(5);

        PdfPCell contentCell = new PdfPCell();
        contentCell.setPadding(5);
        contentCell.setBorder(0);

        addTableHeader(table, headerFont, headerCell);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        addTableCell(table, "Departure", vol.getLieuDepart(), contentFont, contentCell);
        addTableCell(table, "Arrival", vol.getLieuArrivee(), contentFont, contentCell);
        addTableCell(table, "Departure Date", vol.getDateDepart().format(formatter), contentFont, contentCell);
        addTableCell(table, "Arrival Date", vol.getDateArrivee().format(formatter), contentFont, contentCell);
        addTableCell(table, "Flight Duration", vol.getDureeVol() + " min", contentFont, contentCell);
        document.add(table);

        Paragraph footer = new Paragraph("Thank you for flying with us!", smallItalic);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(50);
        document.add(footer);
        document.close();

        JOptionPane.showMessageDialog(this, "PDF de réservation créé avec succès dans le dossier Téléchargements.",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addTableHeader(PdfPTable table, Font font, PdfPCell headerCell) {
        headerCell.setPhrase(new Phrase("Detail", font));
        table.addCell(headerCell);
        headerCell.setPhrase(new Phrase("Value", font));
        table.addCell(headerCell);
    }

    private void addTableCell(PdfPTable table, String header, String value, Font font, PdfPCell cell) {
        cell.setPhrase(new Phrase(header, font));
        table.addCell(cell);
        cell.setPhrase(new Phrase(value, font));
        table.addCell(cell);
    }

    public static void main(String[] args) {
        Session.userId = 1;
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