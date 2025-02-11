import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationGUI extends JFrame {
    private JTextField departField, arriveeField, dateField;
    private JTable volsTable;
    private DefaultTableModel tableModel;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ReservationGUI() {
        setTitle("Réserver un Vol");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de Recherche
        JPanel searchPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Rechercher un Vol"));

        departField = new JTextField();
        arriveeField = new JTextField();
        dateField = new JTextField();

        searchPanel.add(new JLabel("Lieu de départ :"));
        searchPanel.add(departField);
        searchPanel.add(new JLabel("Lieu d’arrivée :"));
        searchPanel.add(arriveeField);
        searchPanel.add(new JLabel("Date (YYYY-MM-DD) :"));
        searchPanel.add(dateField);

        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> rechercherVols());
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        // Table des résultats
        tableModel = new DefaultTableModel(new Object[]{"ID", "Départ", "Arrivée", "Date Départ", "Date Arrivée", "Durée", "Réserver"}, 0);
        volsTable = new JTable(tableModel);

        // Ajout du renderer et de l'éditeur pour les boutons
        volsTable.getColumn("Réserver").setCellRenderer(new ButtonRenderer());
        volsTable.getColumn("Réserver").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(volsTable);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

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
                "Réserver" // Ajout d'un texte à la colonne (le bouton sera ajouté avec l'éditeur)
            });
        }
    }

    // Renderer pour afficher les boutons correctement
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Réserver");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Editor pour gérer l'action du bouton dans la JTable
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Réserver");
            button.addActionListener(e -> reserverVol(selectedRow));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Réserver";
        }
    }

    private void reserverVol(int row) {
        int flightId = (int) tableModel.getValueAt(row, 0); // Récupération de l'ID du vol

        if (Session.userId == -1) {
            JOptionPane.showMessageDialog(this, "Vous devez être connecté pour réserver.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this, "Confirmez-vous la réservation ?", "Confirmation", JOptionPane.OK_CANCEL_OPTION);
        
        if (confirmation == JOptionPane.OK_OPTION) {
            boolean success = ReservationDAO.createReservation(flightId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Réservation réussie !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new ReservationGUI();
    }
}
