import javax.swing.*;
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

    public ReservationGUI() {
        setTitle("Réserver un Vol");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createNavBar();
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        createSearchPanel(mainPanel);
        createResultTable(mainPanel);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void createNavBar() {
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navBar.setBackground(primaryColor);

        String[] menuItems = {"Réserver", "Voir réservation", "Connexion"};
        for (String item : menuItems) {
            JButton btn = createNavButton(item);
            navBar.add(btn);
        }

        add(navBar, BorderLayout.NORTH);
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

        //*** PDF DESIGN START***

        // 1. Metadata

        document.addTitle("Reservation Details");
        document.addSubject("Flight Reservation");
        document.addKeywords("flight, reservation, ticket");
        document.addAuthor("Your Company Name"); //Replace with your own

        // 2. Fonts and Colors

        BaseColor primaryColor = new BaseColor(0, 206, 209); // Turquoise (iText's BaseColor)
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, primaryColor);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);

        // 3. Logo (replace "path/to/your/logo.png" with the actual path)

        try {
            String imagePath = "C:\\Users\\badr4\\OneDrive\\Bureau\\hhh.jpg"; // put your path here
            File imageFile = new File(imagePath);
            if (imageFile.exists()) { // Check if file exist
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
        title.setSpacingBefore(10);
        document.add(title);

        // 5. Table

        PdfPTable table = new PdfPTable(2); // 2 columns
        table.setWidthPercentage(100); // Table takes up the whole page width
        table.setSpacingBefore(10);

        //Add headers to table
        addTableHeader(table, headerFont);

        //Add cells to table
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        addTableCell(table, "Flight ID", String.valueOf(vol.getId()), contentFont);
        addTableCell(table, "Departure", vol.getLieuDepart(), contentFont);
        addTableCell(table, "Arrival", vol.getLieuArrivee(), contentFont);
        addTableCell(table, "Departure Date", vol.getDateDepart().format(formatter), contentFont);
        addTableCell(table, "Arrival Date", vol.getDateArrivee().format(formatter), contentFont);
        addTableCell(table, "Flight Duration", vol.getDureeVol() + " min", contentFont);

        document.add(table);
        // 6. Footer

        Paragraph footer = new Paragraph("Thank you for flying with us!", contentFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(30);
        document.add(footer);

        // ***PDF DESIGN END***

        document.close();

        JOptionPane.showMessageDialog(this, "PDF de réservation créé avec succès dans le dossier Téléchargements.",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    // Helper method to add table header
    private void addTableHeader(PdfPTable table, Font font) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(2);
        header.setPhrase(new Phrase("Detail", font));
        table.addCell(header);

        header = new PdfPCell(); // Reset header
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(2);
        header.setPhrase(new Phrase("Value", font));
        table.addCell(header);
    }

    // Helper method to add a table cell
    private void addTableCell(PdfPTable table, String header, String value, Font font) {
        PdfPCell cellHeader = new PdfPCell(new Phrase(header, font));
        cellHeader.setPaddingLeft(5);
        cellHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cellHeader);

        PdfPCell cellValue = new PdfPCell(new Phrase(value, font));
        cellValue.setPaddingLeft(5);
        cellValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cellValue);
    }

    public static void main(String[] args) {
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