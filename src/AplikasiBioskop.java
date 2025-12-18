package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AplikasiBioskop extends JFrame {
    private JComboBox<String> cbFilm;
    private JPanel panelKursi;
    private JButton[] kursiButtons;
    private JButton btnBayar;
    private JTextArea areaStruk;

    // Data Film
    private String[] daftarFilm = {"Avatar: The Way of Water", "Spider-Man: No Way Home", "Mario Bros Movie"};
    private int[] hargaFilm = {50000, 45000, 40000};
    private ArrayList<Integer> kursiTerpilih = new ArrayList<>();

    public AplikasiBioskop() {
        setTitle("Sistem Bioskop - Kelompok 3");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel Atas
        JPanel pnlAtas = new JPanel(new FlowLayout());
        pnlAtas.add(new JLabel("Pilih Film:"));
        cbFilm = new JComboBox<>(daftarFilm);
        pnlAtas.add(cbFilm);
        add(pnlAtas, BorderLayout.NORTH);

        // Panel Tengah (Kursi)
        panelKursi = new JPanel(new GridLayout(4, 5, 8, 8));
        panelKursi.setBorder(BorderFactory.createTitledBorder("Layar Bioskop"));
        kursiButtons = new JButton[20];
        for (int i = 0; i < 20; i++) {
            kursiButtons[i] = new JButton("A" + (i + 1));
            kursiButtons[i].setBackground(Color.GREEN);
            final int index = i;
            kursiButtons[i].addActionListener(e -> toggleKursi(index));
            panelKursi.add(kursiButtons[i]);
        }
        add(panelKursi, BorderLayout.CENTER);

        // Panel Bawah
        JPanel pnlBawah = new JPanel(new BorderLayout());
        btnBayar = new JButton("BAYAR SEKARANG");
        areaStruk = new JTextArea(8, 30);
        areaStruk.setEditable(false);
        pnlBawah.add(btnBayar, BorderLayout.NORTH);
        pnlBawah.add(new JScrollPane(areaStruk), BorderLayout.CENTER);
        add(pnlBawah, BorderLayout.SOUTH);

        btnBayar.addActionListener(e -> prosesBayar());
    }

    private void toggleKursi(int index) {
        JButton btn = kursiButtons[index];
        if (!btn.isEnabled()) return;
        if (btn.getBackground() == Color.GREEN) {
            btn.setBackground(Color.RED);
            kursiTerpilih.add(index);
        } else {
            btn.setBackground(Color.GREEN);
            kursiTerpilih.remove(Integer.valueOf(index));
        }
    }

    private void prosesBayar() {
        try {
            if (kursiTerpilih.isEmpty()) throw new PembayaranGagalException("Pilih kursi dulu!");

            int idxFilm = cbFilm.getSelectedIndex();
            // Integrasi Kode Fathia
            Tiket tiket = new TiketBioskop(daftarFilm[idxFilm], hargaFilm[idxFilm]);
            int totalHarga = tiket.hitungTotalHarga(kursiTerpilih.size());

            String input = JOptionPane.showInputDialog(this, "Total: Rp " + totalHarga + "\nMasukkan Uang:");
            if (input == null) return;

            int uangBayar = Integer.parseInt(input);

            if (uangBayar < totalHarga) {
                // Integrasi Kode Afliana
                throw new PembayaranGagalException("Uang kurang Rp " + (totalHarga - uangBayar));
            }

            int kembalian = uangBayar - totalHarga;
            areaStruk.setText("SUKSES!\nFilm: " + tiket.getJudulFilm() + "\nKembali: Rp " + kembalian);

            for (int idx : kursiTerpilih) {
                kursiButtons[idx].setBackground(Color.GRAY);
                kursiButtons[idx].setEnabled(false);
            }
            kursiTerpilih.clear();

        } catch (PembayaranGagalException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input harus angka!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AplikasiBioskop().setVisible(true));
    }
}