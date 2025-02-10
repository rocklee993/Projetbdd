import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextArea outputArea;

    public LoginGUI() {
        setTitle("Se Connecter");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.CYAN);
        JLabel titleLabel = new JLabel("Connexion");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel);

        // Main Panel with Grey Background
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Creating Labels and Input Fields
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        emailField.setBackground(Color.WHITE);
        emailField.setFont(new Font("Arial", Font.PLAIN, 12)); // Adjusting font size for smaller height
        emailField.setPreferredSize(new Dimension(200, 30)); // Adjusting the height
        panel.add(emailField);

        panel.add(new JLabel("Mot de passe:"));
        passwordField = new JPasswordField();
        passwordField.setBackground(Color.WHITE);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12)); // Adjusting font size for smaller height
        passwordField.setPreferredSize(new Dimension(200, 30)); // Adjusting the height
        panel.add(passwordField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        JButton loginButton = new JButton("Se connecter");
        loginButton.setBackground(new Color(66, 135, 245));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(200, 40)); // Make the button bigger
        buttonPanel.add(loginButton);

        // Output Area
        outputArea = new JTextArea(3, 30);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Add Components to Frame
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button Action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginAction();
            }
        });

        setVisible(true);
    }

    // Action for login
    private void loginAction() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        // Sample validation (could be replaced with database or user list check)
        if (email.equals("admin@example.com") && password.equals("admin123")) {
            outputArea.setText("Connexion réussie! Bienvenue Admin.");
        } else if (email.equals("user@example.com") && password.equals("user123")) {
            outputArea.setText("Connexion réussie! Bienvenue Utilisateur.");
        } else {
            outputArea.setText("Identifiants incorrects, réessayez.");
        }
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}
