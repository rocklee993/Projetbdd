import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class AccueilGUI extends JFrame {

    public AccueilGUI() {
        setTitle("Bienvenue");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panneau principal avec image de fond
        JPanel backgroundPanel = new JPanel() {
            private BufferedImage image;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                	image = ImageIO.read(new File("C:\\Users\\badr4\\OneDrive\\Bureau\\vol.jpg"));  // Remplacez par le chemin de votre image
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // Adapte l'image à la taille du panneau
                } catch (IOException e) {
                    System.err.println("Image introuvable: " + e.getMessage());
                }
            }
        };

        backgroundPanel.setLayout(new GridBagLayout()); // Pour centrer les boutons

        // Boutons
        JButton inscriptionButton = createStyledButton("Inscription");
        JButton connexionButton = createStyledButton("Connexion");

        // Action listeners
        inscriptionButton.addActionListener(e -> {
            new SignupGUI();
            dispose();
        });

        connexionButton.addActionListener(e -> {
            new LoginGUI();
            dispose();
        });

        // Configuration de la disposition avec GridBagLayout pour centrer les boutons
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // Marge autour des boutons

        backgroundPanel.add(inscriptionButton, gbc);
        backgroundPanel.add(connexionButton, gbc);

        add(backgroundPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 150, 136)); // Teal
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 150, 136).darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 150, 136));
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccueilGUI());
    }
}