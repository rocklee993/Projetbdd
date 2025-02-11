import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextArea outputArea;
    private Color primaryColor = new Color(0, 206, 209); // Turquoise

    public LoginGUI() {
        setTitle("Se Connecter");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(primaryColor);
        JLabel titleLabel = new JLabel("Connexion");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Main Panel
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Mot de passe:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton loginButton = createStyledButton("Se connecter", primaryColor);
        JButton signupButton = createStyledButton("Créer un compte", new Color(34, 139, 34));

        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

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
        loginButton.addActionListener(e -> loginAction());
        signupButton.addActionListener(e -> {
            new SignupGUI();
            dispose();
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

    private void loginAction() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String role = LoginDAO.verifyUser(email, password);

        if (role.equals("admin")) {
            outputArea.setText("✅ Connexion réussie en tant qu'Admin!");
            new VolGUI();
            dispose();
        } else if (role.equals("user")) {
            outputArea.setText("✅ Connexion réussie!");
            new ReservationGUI();
            dispose();
        } else {
            outputArea.setText("❌ Identifiants incorrects, réessayez.");
        }
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}