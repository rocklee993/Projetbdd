import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class LoginGUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextArea outputArea;
    private Color primaryColor = new Color(33, 150, 243); // A darker Blue
    private Color secondaryColor = new Color(240, 240, 240); // Off-White
    private Color textColor = new Color(30, 30, 30); // Very Dark Gray

    public LoginGUI() {
        setTitle("Se Connecter");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel with background image
        JPanel mainPanel = new JPanel() {
            private BufferedImage image;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    image = ImageIO.read(new File("C:\\Users\\badr4\\OneDrive\\Bureau\\vol1.jpg"));  // Replace with your image path
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // Scale image to fit the panel
                } catch (IOException e) {
                    System.err.println("Image introuvable: " + e.getMessage());
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        headerPanel.setOpaque(false); // Make header transparent
        JLabel titleLabel = new JLabel("Connexion");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32)); // Increased size
        titleLabel.setForeground(primaryColor);
        headerPanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2, 10, 25)); // Increased vertical gap
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // Adjusted margins
        formPanel.setOpaque(false); // Make panel transparent

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(textColor);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Larger font
        emailField = new JTextField();
        styleTextField(emailField);

        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordLabel.setForeground(textColor);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Larger font
        passwordField = new JPasswordField();
        styleTextField(passwordField);

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // Adjust horizontal gap
        buttonPanel.setOpaque(false); // Make panel transparent

        JButton loginButton = createStyledButton("Se connecter", primaryColor);
        JButton signupButton = createStyledButton("Créer un compte", new Color(63, 81, 181)); // Darker Indigo-ish

        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        // Output Area
        outputArea = new JTextArea(3, 30);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        outputArea.setOpaque(false); // Make transparent
        outputArea.setForeground(textColor);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14)); // Increased font size

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false); // Make panel transparent
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        // Add Components to Frame
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

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
        button.setForeground(secondaryColor);
        button.setPreferredSize(new Dimension(180, 45)); // Increased size
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Increased font size
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

    private void styleTextField(JTextField textField) {
        textField.setPreferredSize(new Dimension(250, 35)); // Increased height
        textField.setFont(new Font("Arial", Font.PLAIN, 16)); // Increased font size
        textField.setForeground(textColor);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)), // Darker border
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
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