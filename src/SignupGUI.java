import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupGUI extends JFrame {
    private JTextField firstNameField, lastNameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JTextArea outputArea;

    public SignupGUI() {
        setTitle("S'inscrire");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.CYAN);
        JLabel titleLabel = new JLabel("Inscription");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel);

        // Main Panel with Grey Background
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Creating Labels and Input Fields
        panel.add(new JLabel("Prénom:"));
        firstNameField = new JTextField();
        panel.add(firstNameField);

        panel.add(new JLabel("Nom:"));
        lastNameField = new JTextField();
        panel.add(lastNameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Mot de passe:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Confirmer mot de passe:"));
        confirmPasswordField = new JPasswordField();
        panel.add(confirmPasswordField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        JButton signupButton = new JButton("S'inscrire");
        signupButton.setBackground(new Color(66, 135, 245));
        signupButton.setForeground(Color.WHITE);
        signupButton.setPreferredSize(new Dimension(200, 40));
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

        // Button Action
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signupAction();
            }
        });

        setVisible(true);
    }

    // Action for signup
    private void signupAction() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            outputArea.setText("❌ Les mots de passe ne correspondent pas.");
            return;
        }

        // Validate email format (simple validation)
        if (!email.contains("@")) {
            outputArea.setText("❌ L'email est invalide.");
            return;
        }

        // Call the SignupDAO to register the user in the database
        boolean success = SignupDAO.registerUser(firstName, lastName, email, password);

        // Check if registration was successful
        if (success) {
            outputArea.setText("✅ Inscription réussie! Redirection...");

            // Delay for 1.5 seconds before switching to LoginGUI
            Timer timer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new LoginGUI(); // Open LoginGUI
                    dispose(); // Close SignupGUI
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            outputArea.setText("❌ L'email est déjà utilisé.");
        }
    }
}
