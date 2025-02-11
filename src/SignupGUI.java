import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupGUI extends JFrame {
    // Declare input fields and other components
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextArea outputArea;
    private Color primaryColor = new Color(0, 206, 209); // Turquoise

    public SignupGUI() {
        setTitle("S'inscrire");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(primaryColor);
        JLabel titleLabel = new JLabel("Inscription");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Main Panel
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(Color.WHITE);
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
        buttonPanel.setBackground(Color.WHITE);

        JButton signupButton = createStyledButton("S'inscrire", primaryColor);
        JButton loginButton = createStyledButton("Se connecter", new Color(245, 66, 66));

        buttonPanel.add(signupButton);
        buttonPanel.add(loginButton);

        // Output Area
        outputArea = new JTextArea(3, 30);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Add Components to Frame
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button Actions
        signupButton.addActionListener(e -> signupAction());
        loginButton.addActionListener(e -> {
            new LoginGUI(); // Open LoginGUI
            dispose(); // Close SignupGUI
        });

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 40));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        return button;
    }

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

    public static void main(String[] args) {
        new SignupGUI(); // Launch the GUI application
    }
}
