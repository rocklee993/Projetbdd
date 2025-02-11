import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationGUI extends JFrame {
    private JTextField departField, arriveeField, dateField;
    private JTable volsTable;
    private DefaultTableModel tableModel;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Color primaryColor = new Color(0, 206, 209); // Turquoise

    public ReservationGUI() {
        setTitle("Réserver un Vol");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Barre de navigation
        createNavBar();

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de Recherche avec nouveau design
        createSearchPanel(mainPanel);

        // Table des résultats (gardant la logique originale)
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

    private void createSearchPanel(JPanel mainPanel) {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Champs de recherche originaux avec nouveau style
        departField = createStyledTextField();
        arriveeField = createStyledTextField();
        dateField = createStyledTextField();

        // Première ligne
        addLabelAndField(searchPanel, gbc, "Départ", departField, 0);
        addLabelAndField(searchPanel, gbc, "Arrivée", arriveeField, 1);

        // Deuxième ligne
        addLabelAndField(searchPanel, gbc, "Date (YYYY-MM-DD)", dateField, 2);

        // Bouton de recherche
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
        // Création du modèle de table avec les mêmes colonnes que l'original
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Départ", "Arrivée", "Date Départ", "Date Arrivée", "Durée", "Réserver"}, 
            0
        );
        volsTable = new JTable(tableModel);

        // Style de la table
        volsTable.setRowHeight(35);
        volsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        volsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Configuration des boutons comme dans l'original
        volsTable.getColumn("Réserver").setCellRenderer(new ButtonRenderer());
        volsTable.getColumn("Réserver").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(volsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    // Méthodes utilitaires pour le style
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
        label.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(label, gbc);

        gbc.gridy++;
        panel.add(field, gbc);
    }

    private void styleButton(JButton button) {
        button.setBackground(primaryColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 30));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
    }

    // Garder la logique de recherche originale
    private void rechercherVols() {
        String depart = departField.getText();
        String arrivee = arriveeField.getText();
        String date = dateField.getText();

        List<Vol> vols = ReservationDAO.rechercherVols(depart, arrivee, date);
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

    // Classes internes originales pour les boutons
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

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Réserver");
            styleButton(button);
            button.addActionListener(e -> reserverVol(selectedRow));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, 
                boolean isSelected, int row, int column) {
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Réserver";
        }
    }

    private void reserverVol(int row) {
        int flightId = (int) tableModel.getValueAt(row, 0);

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
                JOptionPane.showMessageDialog(this, "Réservation réussie !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la réservation.", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
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