import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The ReservationGUI class represents the graphical user interface (GUI) for flight reservation.
 * It allows the user to search for flights based on departure, arrival, and date, and to make a reservation
 * by interacting with a table displaying the search results.
 */
public class ReservationGUI extends JFrame {
	/**
	 * The JTextField for entering the departure location.
	 */
	private JTextField departField;

	/**
	 * The JTextField for entering the arrival location.
	 */
	private JTextField arriveeField;

	/**
	 * The JTextField for entering the date.
	 */
	private JTextField dateField;

	/**
	 * The JTable for displaying flight information.
	 */
	private JTable volsTable;

	/**
	 * The DefaultTableModel used for managing the table data in the JTable.
	 */
	private DefaultTableModel tableModel;

	/**
	 * The DateTimeFormatter used to format date and time in the "yyyy-MM-dd HH:mm" pattern.
	 */
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    /**
     * Constructor for the ReservationGUI class. It initializes the window layout, search panel, 
     * table for displaying flight results, and the "Rechercher" button.
     */
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

    /**
     * Method to search for flights based on the entered search criteria (departure location, arrival location, and date).
     * The method fetches the flight data and updates the table with the search results.
     */
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

    /**
     * Inner class ButtonRenderer that extends JButton and implements TableCellRenderer to render a button in the "Réserver" column.
     * The button appears in the table for each flight entry to allow users to make a reservation.
     */
    class ButtonRenderer extends JButton implements TableCellRenderer {
        /**
         * Constructor for the ButtonRenderer.
         * Sets the text of the button to "Réserver".
         */
        public ButtonRenderer() {
            setText("Réserver");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this; // Return the button to be rendered in the table cell
        }
    }

    /**
     * Inner class ButtonEditor extends DefaultCellEditor to handle the action when a user clicks the "Réserver" button.
     * It handles the logic of selecting a flight to reserve.
     */
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int selectedRow;

        /**
         * Constructor for the ButtonEditor.
         * Initializes the button and adds an action listener to handle the button click.
         *
         * @param checkBox the checkbox to be passed to the DefaultCellEditor constructor
         */
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Réserver");
            button.addActionListener(e -> reserverVol(selectedRow));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            selectedRow = row; // Store the selected row
            return button; // Return the button to be displayed in the table cell
        }

        @Override
        public Object getCellEditorValue() {
            return "Réserver"; // Return the text displayed on the button
        }
    }

    /**
     * Method to handle the reservation of a selected flight.
     * If the user is not logged in, it prompts them to log in. It asks for a confirmation before creating the reservation.
     *
     * @param row the row of the flight in the table that the user wants to reserve
     */
    private void reserverVol(int row) {
        int flightId = (int) tableModel.getValueAt(row, 0); // Retrieve the ID of the selected flight

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

    /**
     * The main method to launch the ReservationGUI application.
     * Creates a new instance of the ReservationGUI class to display the flight reservation interface.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new ReservationGUI(); // Launch the GUI application
    }
}
