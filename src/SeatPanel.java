package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class SeatPanel extends JPanel {
    private AplikasiBioskop mainApp;
    private JPanel gridKursi;
    private KursiButton[] buttons = new KursiButton[30];
    private JLabel lblInfo;

    public SeatPanel(AplikasiBioskop app) {
        this.mainApp = app;
        setLayout(new BorderLayout());
        setBackground(AplikasiBioskop.CLR_DARK);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(20, 50, 20, 50));

        AplikasiBioskop.RoundedButton btnBack = new AplikasiBioskop.RoundedButton("← Movies");
        btnBack.setBackgroundColor(new Color(255, 255, 255, 20));
        btnBack.setPreferredSize(new Dimension(110, 40));
        btnBack.addActionListener(e -> {
            mainApp.resetSelection();
            mainApp.showPage("LANDING");
        });

        lblInfo = new JLabel("", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Cinzel", Font.BOLD, 26));
        lblInfo.setForeground(AplikasiBioskop.CLR_GOLD);

        header.add(btnBack, BorderLayout.WEST);
        header.add(lblInfo, BorderLayout.CENTER);
        header.add(Box.createHorizontalStrut(110), BorderLayout.EAST);

        // Screen
        JPanel screenPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Path2D.Float path = new Path2D.Float();
                path.moveTo(100, 20);
                path.lineTo(getWidth()-100, 20);
                path.lineTo(getWidth(), 120);
                path.lineTo(0, 120);
                path.closePath();

                GradientPaint gp = new GradientPaint(
                        getWidth()/2, 20, new Color(255, 255, 255, 40),
                        getWidth()/2, 120, new Color(0, 0, 0, 0)
                );
                g2.setPaint(gp);
                g2.fill(path);
                g2.setColor(new Color(255,255,255,150));
                g2.drawString("CINEMA SCREEN", getWidth()/2 - 40, 50);
            }
        };
        screenPanel.setPreferredSize(new Dimension(0, 140));
        screenPanel.setOpaque(false);

        // Grid Kursi
        gridKursi = new JPanel(new GridLayout(5, 6, 20, 20));
        gridKursi.setOpaque(false);
        gridKursi.setBorder(new EmptyBorder(10, 250, 50, 250));

        for(int i=0; i<30; i++) {
            // PERBAIKAN: Gunakan Logic dari Main App biar nama kursi KONSISTEN
            String namaKursi = mainApp.getSeatName(i);
            buttons[i] = new KursiButton(namaKursi);

            final int idx = i;
            buttons[i].addActionListener(e -> toggleSeat(idx));
            gridKursi.add(buttons[i]);
        }

        // Footer
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(0, 0, 40, 0));
        AplikasiBioskop.RoundedButton btnNext = new AplikasiBioskop.RoundedButton("Checkout");
        btnNext.setBackgroundColor(AplikasiBioskop.CLR_GOLD);
        btnNext.setForeground(AplikasiBioskop.CLR_PRIMARY);
        btnNext.setPreferredSize(new Dimension(280, 55));
        btnNext.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnNext.addActionListener(e -> {
            if(mainApp.getCurrentSeats().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one seat.");
            } else {
                mainApp.getPaymentPanel().updateDetails();
                mainApp.showPage("PAYMENT");
            }
        });
        footer.add(btnNext);

        add(header, BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(screenPanel, BorderLayout.NORTH);
        center.add(gridKursi, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    public void refreshSeats() {
        lblInfo.setText(mainApp.getSelectedFilm());
        ArrayList<Integer> sold = mainApp.getSoldSeats(mainApp.getSelectedFilm());
        for(int i=0; i<30; i++) {
            buttons[i].setSold(sold.contains(i));
            buttons[i].setSelected(false);
        }
    }

    private void toggleSeat(int idx) {
        KursiButton btn = buttons[idx];
        if(btn.isSold) return;
        if(btn.isSelected) {
            mainApp.getCurrentSeats().remove(Integer.valueOf(idx));
            btn.setSelected(false);
        } else {
            mainApp.getCurrentSeats().add(idx);
            btn.setSelected(true);
        }
    }

    private class KursiButton extends JButton {
        boolean isSelected = false, isSold = false;
        String txt;
        public KursiButton(String t) {
            this.txt = t;
            setPreferredSize(new Dimension(60, 75));
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        public void setSold(boolean b) { isSold = b; repaint(); }
        public void setSelected(boolean b) { isSelected = b; repaint(); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color c = isSold ? new Color(220, 38, 38) : (isSelected ? AplikasiBioskop.CLR_GOLD : new Color(71, 85, 105));
            g2.setColor(c);
            g2.fillRoundRect(5, 0, getWidth()-10, 15, 8, 8);
            g2.fillRoundRect(2, 18, getWidth()-4, 28, 8, 8);
            g2.fillRoundRect(2, 50, getWidth()-4, 20, 8, 8);
            g2.setColor(c.darker());
            g2.fillRoundRect(0, 28, 6, 35, 5, 5);
            g2.fillRoundRect(getWidth()-6, 28, 6, 35, 5, 5);
            g2.setColor(isSelected ? AplikasiBioskop.CLR_PRIMARY : Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(txt, (getWidth()-fm.stringWidth(txt))/2, 38);
        }
    }
}