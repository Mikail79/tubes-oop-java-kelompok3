package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PaymentPanel extends JPanel {
    private AplikasiBioskop mainApp;
    private JLabel lblFilm, lblTotal;
    private JTextArea txtSeatsList;
    private JTextField txtMoney;
    private NumberFormat rp = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public PaymentPanel(AplikasiBioskop app) {
        this.mainApp = app;
        setLayout(new GridBagLayout());
        setBackground(AplikasiBioskop.CLR_WHITE);

        JPanel card = new JPanel(new GridLayout(0, 1, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(new Color(0,0,0,20));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,30,30);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(450, 600));
        card.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Payment Summary", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(AplikasiBioskop.CLR_DARK);

        JLabel lblFilmKey = new JLabel("Movie:");
        lblFilmKey.setForeground(AplikasiBioskop.CLR_DARK);
        lblFilm = new JLabel("-");
        lblFilm.setForeground(AplikasiBioskop.CLR_PRIMARY);

        JLabel lblSeatHeader = new JLabel("Selected Seats:");
        lblSeatHeader.setForeground(Color.GRAY);

        txtSeatsList = new JTextArea();
        txtSeatsList.setFont(new Font("SansSerif", Font.BOLD, 14));
        txtSeatsList.setForeground(AplikasiBioskop.CLR_DARK);
        txtSeatsList.setLineWrap(true);
        txtSeatsList.setWrapStyleWord(true);
        txtSeatsList.setEditable(false);
        txtSeatsList.setOpaque(false);

        lblTotal = new JLabel("Total:");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTotal.setForeground(AplikasiBioskop.CLR_PRIMARY);

        txtMoney = new JTextField();
        txtMoney.setFont(new Font("SansSerif", Font.BOLD, 18));
        txtMoney.setHorizontalAlignment(JTextField.CENTER);
        txtMoney.setBorder(BorderFactory.createMatteBorder(0,0,2,0, AplikasiBioskop.CLR_PRIMARY));

        AplikasiBioskop.RoundedButton btnPay = new AplikasiBioskop.RoundedButton("Confirm Payment");
        btnPay.setBackgroundColor(AplikasiBioskop.CLR_GOLD);
        btnPay.setForeground(Color.BLACK);
        btnPay.setPreferredSize(new Dimension(0, 50));
        btnPay.addActionListener(e -> processPayment());

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setContentAreaFilled(false);
        btnCancel.setBorderPainted(false);
        btnCancel.setForeground(Color.GRAY);
        btnCancel.addActionListener(e -> mainApp.showPage("SEAT"));

        card.add(title);
        card.add(new JSeparator());
        card.add(lblFilmKey);
        card.add(lblFilm);
        card.add(lblSeatHeader);
        card.add(txtSeatsList);
        card.add(new JSeparator());
        card.add(lblTotal);
        card.add(new JLabel("Cash Amount (IDR):"));
        card.add(txtMoney);
        card.add(Box.createVerticalStrut(10));
        card.add(btnPay);
        card.add(btnCancel);

        add(card);
    }

    public void updateDetails() {
        int total = mainApp.getCurrentSeats().size() * mainApp.getSelectedPrice();
        ArrayList<String> seatNames = new ArrayList<>();
        // PERBAIKAN: Panggil getSeatName dari Main App biar SAMA
        for(int idx : mainApp.getCurrentSeats()) {
            seatNames.add(mainApp.getSeatName(idx));
        }

        lblFilm.setText(mainApp.getSelectedFilm());
        txtSeatsList.setText(String.join(", ", seatNames));
        lblTotal.setText(rp.format(total));
        txtMoney.setText("");
    }

    private void processPayment() {
        try {
            int total = mainApp.getCurrentSeats().size() * mainApp.getSelectedPrice();
            if(txtMoney.getText().isEmpty()) return;
            int uang = Integer.parseInt(txtMoney.getText());
            if(uang < total) {
                JOptionPane.showMessageDialog(this, "Insufficient funds!");
                return;
            }
            mainApp.addSoldSeats(mainApp.getSelectedFilm(), mainApp.getCurrentSeats());
            mainApp.getReceiptPanel().generateReceipt(uang, uang-total);
            mainApp.showPage("RECEIPT");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number");
        }
    }
}