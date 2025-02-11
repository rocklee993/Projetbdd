import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ReservationGUI extends JFrame {
    private JTextField departureField, arrivalField, dateField;
    private JButton searchButton, reserveButton;
    private JTextArea flightListArea;
    private List<Vol> availableFlights; // List of flights matching search criteria

    public ReservationGUI() {
        setTitle("Réservation de Vol");
        setSize(500, 400); // Adjusting the size to match the LoginGUI size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.CYAN);
        JLabel titleLabel = new JLabel("Réservation de Vol");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel);

        // Main Panel with Grey Background
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search Fields for Departure, Arrival, Date
        panel.add(new JLabel("Lieu Départ:"));
        departureField = new JTextField();
        departureField.setBackground(Color.WHITE);
        departureField.setFont(new Font("Arial", Font.PLAIN, 12)); // Adjusting font size
        departureField.setPreferredSize(new Dimension(200, 30)); // Adjusting the height
        panel.add(departureField);

        panel.add(new JLabel("Lieu Arrivée:"));
        arrivalField = new JTextField();
        arrivalField.setBackground(Color.WHITE);
        arrivalField.setFont(new Font("Arial", Font.PLAIN, 12));
        arrivalField.setPreferredSize(new Dimension(200, 30));
        panel.add(arrivalField);

        panel.add(new JLabel("Date (yyyy-MM-dd):"));
        dateField = new JTextField();
        dateField.setBackground(Color.WHITE);
        dateField.setFont(new Font("Arial", Font.PLAIN, 12));
        dateField.setPreferredSize(new Dimension(200, 30));
        panel.add(dateField);

        // Search Button
        searchButton = new JButton("Chercher");
        searchButton.setBackground(new Color(66, 135, 245));
        searchButton.setForeground(Color.WHITE);
        searchButton.setPreferredSize(new Dimension(200, 40));
        panel.add(searchButton);
        panel.add(new JLabel()); // Empty label for spacing

        // Flight List Area to display available flights
        flightListArea = new JTextArea(6, 30);
        flightListArea.setEditable(false);
        flightListArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        // Bottom Panel with Reserve Button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        reserveButton = new JButton("Réserver");
        reserveButton.setBackground(new Color(66, 135, 245));
        reserveButton.setForeground(Color.WHITE);
        reserveButton.setPreferredSize(new Dimension(200, 40));
        bottomPanel.add(reserveButton, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(flightListArea), BorderLayout.CENTER);

        // Add Components to Frame
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button Actions
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //searchFlights();
            }
        });

        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reserveFlight();
            }
        });

        setVisible(true);
    }

    /*private void searchFlights() {
        // Here, you would query the available flights based on the search parameters
        // For simplicity, let's assume the available flights are already loaded in a list
        availableFlights = FlightDatabase.searchFlights(departureField.getText(), arrivalField.getText(), dateField.getText());

        // Display the available flights in the text area
        StringBuilder sb = new StringBuilder();
        for (Vol vol : availableFlights) {
            sb.append(vol.getId() + " - " + vol.getLieuDepart() + " -> " + vol.getLieuArrivee() + "\n");
        }
        flightListArea.setText(sb.toString());
    }*/

    private void reserveFlight() {
        // Assume that the user is logged in and we have access to their details
        Person currentUser = getCurrentUser(); // Method to get the current user (e.g., from a login system)
        if (availableFlights.isEmpty()) {
            flightListArea.setText("Aucun vol disponible pour la recherche.");
            return;
        }

        // Let's assume the user selects the first flight (for simplicity)
        Vol selectedFlight = availableFlights.get(0);

        // Create a new Reservation
        Reservation reservation = new Reservation(generateReservationId(), currentUser, selectedFlight);
        flightListArea.setText("Réservation effectuée!\n" + reservation);
    }

    // Generate a random Reservation ID (this could be replaced with a more robust ID generation system)
    private int generateReservationId() {
        return (int) (Math.random() * 10000);
    }

    // Example method to simulate the current logged-in user
    private Person getCurrentUser() {
        return new Person(1, "John", "Doe", "john.doe@example.com", "password123");
    }

    /*public static void main(String[] args) {
        new ReservationGUI();
    }*/
}
