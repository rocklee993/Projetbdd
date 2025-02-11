import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AfficherVolGUI extends JFrame {
    private JTable volsTable;
    private DefaultTableModel tableModel;
    private VolDAO volDAO;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Color primaryColor = new Color(0, 206, 209); // Turquoise

    public AfficherVolGUI() {
        volDAO = new VolDAO();
        
        setTitle("Liste des Vols");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Navigation Bar
        createNavBar();

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Search Panel
        createSearchPanel(mainPanel);

        // Table Panel
        createTablePanel(mainPanel);

        add(mainPanel, BorderLayout.CENTER);

        // Load initial data
        refreshVolsTable("");
        
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
            } else if (item.equals("Gestion des Vols")) {
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
        new VolGUI(); // Assuming VolGUI is the class for your VolGUI
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
                if (!text.equals("Afficher tous les vols")) {
                    button.setBackground(primaryColor.darker());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!text.equals("Afficher tous les vols")) {
                    button.setBackground(primaryColor);
                }
            }
        });

        return button;
    }

    private void createSearchPanel(JPanel mainPanel) {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel searchLabel = new JLabel("Rechercher:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton searchButton = new JButton("Rechercher");
        styleButton(searchButton);
        
        // Add search functionality
        searchButton.addActionListener(e -> refreshVolsTable(searchField.getText()));

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        

        mainPanel.add(searchPanel, BorderLayout.NORTH);
    }

    private void createTablePanel(JPanel mainPanel) {
        // Create table model with columns
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Lieu Départ", "Lieu Arrivée", "Date Départ", "Date Arrivée", "Durée", "Status"}, 
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make table read-only
            }
        };

        volsTable = new JTable(tableModel);
        
        // Style the table
        volsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        volsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        volsTable.setRowHeight(35);
        volsTable.setShowGrid(true);
        volsTable.setGridColor(Color.LIGHT_GRAY);
        
        // Center align all columns
        centerAlignColumns();

        // Add custom renderer for Status column
        volsTable.getColumn("Status").setCellRenderer(new StatusRenderer());

        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(volsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void centerAlignColumns() {
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < volsTable.getColumnCount(); i++) {
            volsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(primaryColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 30));
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBackground().darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBackground().brighter());
            }
        });
    }

    private void refreshVolsTable(String searchTerm) {
        tableModel.setRowCount(0);  // Clear existing data
        
        List<Vol> vols = volDAO.getTousLesVols();  // Get all flights
        
        for (Vol vol : vols) {
            // If there's a search term, filter the results
            if (searchTerm.isEmpty() || 
                vol.getLieuDepart().toLowerCase().contains(searchTerm.toLowerCase()) ||
                vol.getLieuArrivee().toLowerCase().contains(searchTerm.toLowerCase())) {
                
                // Calculate duration in hours and minutes
                long durationInMinutes = vol.getDureeVol();
                String duration = String.format("%dh %02dm", 
                    durationInMinutes / 60, 
                    durationInMinutes % 60);
                
                // Determine status based on departure time
                String status = determineStatus(vol);
                
                tableModel.addRow(new Object[]{
                    vol.getId(),
                    vol.getLieuDepart(),
                    vol.getLieuArrivee(),
                    vol.getDateDepart().format(formatter),
                    vol.getDateArrivee().format(formatter),
                    duration,
                    status
                });
            }
        }
    }

    private String determineStatus(Vol vol) {
        if (vol.getDateDepart().isAfter(java.time.LocalDateTime.now())) {
            return "À venir";
        } else if (vol.getDateArrivee().isBefore(java.time.LocalDateTime.now())) {
            return "Terminé";
        } else {
            return "En cours";
        }
    }

    // Custom renderer for the Status column
    class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value != null) {
                String status = value.toString();
                switch (status) {
                    case "À venir":
                        setBackground(new Color(173, 216, 230));  // Light blue
                        break;
                    case "En cours":
                        setBackground(new Color(144, 238, 144));  // Light green
                        break;
                    case "Terminé":
                        setBackground(new Color(211, 211, 211));  // Light gray
                        break;
                    default:
                        setBackground(table.getBackground());
                }
            }
            
            setHorizontalAlignment(JLabel.CENTER);
            return c;
        }
    }

    public static void main(String[] args) {
        
            
            new AfficherVolGUI(); 
        
    }

}
