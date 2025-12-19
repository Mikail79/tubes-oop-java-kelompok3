package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ReceiptPanel extends JPanel {
    private AplikasiBioskop mainApp;
    private JTextArea area;
    private NumberFormat rp = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public ReceiptPanel(AplikasiBioskop app) {
        this.mainApp = app;
        setLayout(new GridBagLayout());
        setBackground(AplikasiBioskop.CLR_PRIMARY);

        JPanel paper = new JPanel(new BorderLayout());
        paper.setBackground(AplikasiBioskop.CLR_WHITE);
        paper.setPreferredSize(new Dimension(350, 480));
        paper.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel icon = new JLabel("🎟️", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));

        JLabel header = new JLabel("Booking Success!", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.setForeground(AplikasiBioskop.CLR_DARK);

        area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setEditable(false);
        area.setBackground(AplikasiBioskop.CLR_WHITE);
        area.setBorder(new EmptyBorder(20, 0, 20, 0));

        AplikasiBioskop.RoundedButton btnHome = new AplikasiBioskop.RoundedButton("Back to Home");
        btnHome.setBackgroundColor(AplikasiBioskop.CLR_DARK);
        btnHome.addActionListener(e -> {
            mainApp.resetSelection();
            mainApp.showPage("LANDING");
        });

        JPanel top = new JPanel(new GridLayout(2,1));
        top.setOpaque(false);
        top.add(icon); top.add(header);

        paper.add(top, BorderLayout.NORTH);
        paper.add(area, BorderLayout.CENTER);
        paper.add(btnHome, BorderLayout.SOUTH);

        add(paper);
    }

    public void generateReceipt(int bayar, int kembalian) {
        int total = mainApp.getSelectedPrice() * mainApp.getCurrentSeats().size();

        // PERBAIKAN: Panggil getSeatName agar konsisten
        ArrayList<String> seatNames = new ArrayList<>();
        for(int idx : mainApp.getCurrentSeats()) {
            seatNames.add(mainApp.getSeatName(idx));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n  CINEMAXX PREMIER  \n");
        sb.append("===========================\n");
        sb.append("User   : ").append(mainApp.getCurrentUser()).append("\n");
        sb.append("Movie  : ").append(mainApp.getSelectedFilm()).append("\n");
        sb.append("Seats  : ").append(String.join(", ", seatNames)).append("\n"); // Rapi
        sb.append("---------------------------\n");
        sb.append("Total  : ").append(rp.format(total)).append("\n");
        sb.append("Paid   : ").append(rp.format(bayar)).append("\n");
        sb.append("Change : ").append(rp.format(kembalian)).append("\n");
        sb.append("===========================\n");
        sb.append("  Enjoy the movie!  \n");
        area.setText(sb.toString());
    }
}