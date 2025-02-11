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
        panel.add(emailField);

        panel.add(new JLabel("Mot de passe:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        JButton loginButton = new JButton("Se connecter");
        loginButton.setBackground(new Color(66, 135, 245));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(200, 40));

        JButton signupButton = new JButton("Créer un compte"); // Sign Up Button
        signupButton.setBackground(Color.GREEN);
        signupButton.setForeground(Color.WHITE);
        signupButton.setPreferredSize(new Dimension(200, 40));

        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

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

        // Button Actions
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginAction();
            }
        });

        signupButton.addActionListener(new ActionListener() { // Sign Up Button Action
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignupGUI(); // Open SignupGUI
                dispose(); // Close LoginGUI
            }
        });

        setVisible(true);
    }

    // Action for login
    private void loginAction() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        // Get role from LoginDAO
        String role = LoginDAO.verifyUser(email, password);

        if (role.equals("admin")) {
            outputArea.setText("✅ Connexion réussie en tant qu'Admin!");
            new VolGUI(); // Open VolGUI for admin
            dispose(); // Close LoginGUI
        } else if (role.equals("user")) {
            outputArea.setText("✅ Connexion réussie!");
            new ReservationGUI(); // Open user dashboard (Replace with actual class)
            dispose(); // Close LoginGUI
        } else {
            outputArea.setText("❌ Identifiants incorrects, réessayez.");
        }
    }


    public static void main(String[] args) {
        new LoginGUI();
    }
}
