package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends JPanel {
    private AplikasiBioskop mainApp;
    private JTextField txtUser;

    public LoginPanel(AplikasiBioskop app) {
        this.mainApp = app;
        setLayout(new GridBagLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background Gradient (Dark Theme)
        GradientPaint gp = new GradientPaint(0, 0, AplikasiBioskop.CLR_DARK, 0, getHeight(), AplikasiBioskop.CLR_PRIMARY);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    {
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 20)); // Glass effect
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(new Color(255, 255, 255, 50));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,40,40);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(400, 450));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel lblTitle = new JLabel("Welcome Back", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Cinzel", Font.BOLD, 30));
        lblTitle.setForeground(AplikasiBioskop.CLR_GOLD);

        JLabel lblSub = new JLabel("Cinemaxx Premier Booking", SwingConstants.CENTER);
        lblSub.setForeground(Color.LIGHT_GRAY);

        gbc.gridy = 0; card.add(lblTitle, gbc);
        gbc.gridy = 1; card.add(lblSub, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(40, 20, 10, 20);
        txtUser = new JTextField(15);
        txtUser.setPreferredSize(new Dimension(0, 50));
        txtUser.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtUser.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        txtUser.setText("");
        card.add(txtUser, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(20, 20, 30, 20);

        // Tombol Login
        AplikasiBioskop.RoundedButton btn = new AplikasiBioskop.RoundedButton("Login System");
        btn.setPreferredSize(new Dimension(0, 50));
        btn.setBackgroundColor(AplikasiBioskop.CLR_GOLD);
        btn.setForeground(Color.BLACK);
        btn.addActionListener(e -> {
            mainApp.setCurrentUser(txtUser.getText());
            mainApp.showPage("LANDING");
        });
        card.add(btn, gbc);

        add(card);
    }
}