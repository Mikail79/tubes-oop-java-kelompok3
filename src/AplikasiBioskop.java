package src;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class AplikasiBioskop extends JFrame {

    // --- Palet Warna Premium (Gold & Dark) ---
    private final Color CLR_BG_DARK = new Color(25, 25, 25);
    private final Color CLR_BG_PANEL = new Color(35, 35, 35);
    private final Color CLR_GOLD = new Color(212, 175, 55);
    private final Color CLR_TEXT_LITE = new Color(230, 230, 230);

    // Warna Status Kursi
    private final Color SEAT_AVAIL = new Color(70, 70, 70);
    private final Color SEAT_SELECT = CLR_GOLD;
    private final Color SEAT_SOLD = new Color(180, 40, 40);

    // Komponen UI
    private JComboBox<String> cbFilm;
    private JPanel panelKursiContainer;
    private KursiButton[] kursiButtons;
    private JLabel lblTotalHarga;
    private JButton btnBayar;

    // Data
    private String[] daftarFilm = {"Avatar: The Way of Water", "Spider-Man: No Way Home", "Oppenheimer", "Mission Impossible 7"};
    private int[] hargaFilm = {65000, 50000, 55000, 60000};

    // Logic: Kursi saat ini dipilih user (sementara)
    private ArrayList<Integer> kursiSedangDipilih = new ArrayList<>();

    // Logic: Database kursi terjual PER FILM (Key: Judul, Value: List Index Kursi Terjual)
    private HashMap<String, ArrayList<Integer>> databaseKursi = new HashMap<>();

    // Format Rupiah
    NumberFormat rp = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public AplikasiBioskop() {
        // Inisialisasi Database Kosong untuk tiap film
        for (String f : daftarFilm) {
            databaseKursi.put(f, new ArrayList<>());
        }

        setTitle("ROYAL CINEMA XXI - Reservation");
        setSize(950, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(CLR_BG_DARK);

        // =================================================================
        // 1. HEADER (Mewah dengan Custom ComboBox)
        // =================================================================
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(15, 15, 15));
        pnlHeader.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Logo Text
        JPanel pnlTitle = new JPanel(new GridLayout(2, 1));
        pnlTitle.setOpaque(false);
        JLabel lblBrand = new JLabel("ROYAL CINEMA");
        lblBrand.setFont(new Font("Cinzel", Font.BOLD, 26)); // Font Serif elegan
        lblBrand.setForeground(CLR_GOLD);
        JLabel lblSub = new JLabel("Premium Booking System");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblSub.setForeground(Color.GRAY);
        pnlTitle.add(lblBrand);
        pnlTitle.add(lblSub);

        // Custom Styled ComboBox
        cbFilm = new JComboBox<>(daftarFilm);
        styleComboBox(cbFilm);
        // EVENT LISTENER: Ganti Film -> Reset Kursi -> Load Data Kursi Film tsb
        cbFilm.addActionListener(e -> refreshTampilanKursi());

        pnlHeader.add(pnlTitle, BorderLayout.WEST);
        pnlHeader.add(cbFilm, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // =================================================================
        // 2. CENTER (Layar & Kursi Proporsional)
        // =================================================================
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(CLR_BG_DARK);
        pnlCenter.setBorder(new EmptyBorder(10, 0, 10, 0));

        // -- Visualisasi Layar (Curved Effect) --
        JPanel pnlLayar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gambar Layar Melengkung
                GradientPaint gp = new GradientPaint(0, 0, new Color(200, 200, 200), getWidth(), 0, new Color(50, 50, 50));
                g2.setPaint(gp);
                g2.fillArc(-50, -20, getWidth()+100, 40, 180, 180);

                // Efek Cahaya Proyektor
                GradientPaint light = new GradientPaint(getWidth()/2, 10, new Color(255, 255, 255, 30), getWidth()/2, 150, new Color(0,0,0,0));
                g2.setPaint(light);
                g2.fillPolygon(new int[]{100, getWidth()-100, getWidth(), 0}, new int[]{20, 20, 150, 150}, 4);

                g2.setColor(Color.GRAY);
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                g2.drawString("SCREEN", getWidth()/2 - 20, 35);
            }
        };
        pnlLayar.setPreferredSize(new Dimension(800, 60));
        pnlLayar.setOpaque(false);

        // -- Container Kursi --
        // Gunakan GridBagLayout agar posisi kursi ada di tengah (Center Aligned)
        JPanel wrapperKursi = new JPanel(new GridBagLayout());
        wrapperKursi.setBackground(CLR_BG_DARK);

        // Grid Kursi Utama (5 Baris x 6 Kolom)
        panelKursiContainer = new JPanel(new GridLayout(5, 6, 12, 12)); // Gap 12px
        panelKursiContainer.setOpaque(false);

        kursiButtons = new KursiButton[30];
        for (int i = 0; i < 30; i++) {
            char baris = (char) ('A' + (i / 6));
            int nomor = (i % 6) + 1;
            String label = baris + "" + nomor;

            kursiButtons[i] = new KursiButton(label);
            final int index = i;
            kursiButtons[i].addActionListener(e -> togglePilihKursi(index));
            panelKursiContainer.add(kursiButtons[i]);
        }

        wrapperKursi.add(panelKursiContainer); // Masukkan Grid ke Wrapper Center

        pnlCenter.add(pnlLayar, BorderLayout.NORTH);
        pnlCenter.add(wrapperKursi, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // =================================================================
        // 3. FOOTER (Total & Bayar)
        // =================================================================
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(new Color(15, 15, 15));
        pnlFooter.setBorder(new EmptyBorder(15, 40, 15, 40));

        // Legend Warna
        JPanel pnlLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlLegend.setOpaque(false);
        pnlLegend.add(makeLegendItem(SEAT_AVAIL, "Tersedia"));
        pnlLegend.add(makeLegendItem(SEAT_SELECT, "Dipilih"));
        pnlLegend.add(makeLegendItem(SEAT_SOLD, "Terjual"));

        // Tombol Action
        JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlAction.setOpaque(false);

        lblTotalHarga = new JLabel("Total: Rp 0");
        lblTotalHarga.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTotalHarga.setForeground(CLR_GOLD);

        btnBayar = new JButton("CHECKOUT");
        btnBayar.setPreferredSize(new Dimension(160, 40));
        btnBayar.setBackground(CLR_GOLD);
        btnBayar.setForeground(Color.BLACK);
        btnBayar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBayar.setFocusPainted(false);
        btnBayar.addActionListener(e -> bukaDialogPembayaran());

        pnlAction.add(lblTotalHarga);
        pnlAction.add(btnBayar);

        pnlFooter.add(pnlLegend, BorderLayout.WEST);
        pnlFooter.add(pnlAction, BorderLayout.EAST);
        add(pnlFooter, BorderLayout.SOUTH);

        // Refresh awal agar status kursi benar
        refreshTampilanKursi();
    }

    // --- Helper: Styling ComboBox agar Dark Mode ---
    private void styleComboBox(JComboBox<String> box) {
        box.setFont(new Font("SansSerif", Font.PLAIN, 14));
        box.setBackground(CLR_BG_PANEL);
        box.setForeground(CLR_GOLD);
        box.setPreferredSize(new Dimension(280, 40));
        // Renderer untuk isi dropdown
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    setBackground(CLR_GOLD);
                    setForeground(Color.BLACK);
                } else {
                    setBackground(CLR_BG_PANEL);
                    setForeground(Color.WHITE);
                }
                setBorder(new EmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
    }

    // --- Helper: Widget Legend ---
    private JPanel makeLegendItem(Color c, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setOpaque(false);
        JPanel box = new JPanel();
        box.setPreferredSize(new Dimension(12, 12));
        box.setBackground(c);
        JLabel l = new JLabel(text);
        l.setForeground(Color.GRAY);
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        p.add(box);
        p.add(l);
        return p;
    }

    // =================================================================
    // LOGIC: Kursi & Data
    // =================================================================

    // 1. Refresh Kursi saat Ganti Film
    private void refreshTampilanKursi() {
        String judulFilm = (String) cbFilm.getSelectedItem();

        // Ambil data kursi terjual untuk film ini
        ArrayList<Integer> terjual = databaseKursi.get(judulFilm);

        // Reset Pilihan User
        kursiSedangDipilih.clear();
        updateLabelTotal();

        // Loop semua tombol, update statusnya
        for (int i = 0; i < 30; i++) {
            kursiButtons[i].resetStatus(); // Reset ke available dulu

            if (terjual.contains(i)) {
                kursiButtons[i].setSold(true);
            }
        }
        panelKursiContainer.repaint();
    }

    // 2. Klik Kursi
    private void togglePilihKursi(int index) {
        KursiButton btn = kursiButtons[index];
        if (btn.isSold) return; // Jangan respon jika terjual

        if (!btn.isSelected) {
            // Pilih
            btn.setSelected(true);
            kursiSedangDipilih.add(index);
        } else {
            // Batal Pilih
            btn.setSelected(false);
            kursiSedangDipilih.remove(Integer.valueOf(index));
        }
        updateLabelTotal();
    }

    private void updateLabelTotal() {
        int idx = cbFilm.getSelectedIndex();
        int total = hargaFilm[idx] * kursiSedangDipilih.size();
        lblTotalHarga.setText("Total: " + rp.format(total));
    }

    // =================================================================
    // LOGIC: Pembayaran Proper (Dialog Keren)
    // =================================================================
    private void bukaDialogPembayaran() {
        if (kursiSedangDipilih.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih kursi dulu, Bos!", "Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idx = cbFilm.getSelectedIndex();
        String judul = daftarFilm[idx];
        int hargaSatuan = hargaFilm[idx];
        int totalTagihan = hargaSatuan * kursiSedangDipilih.size();

        // 1. Setup Dialog Modal
        JDialog dialog = new JDialog(this, "Payment Gateway", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(CLR_BG_DARK);

        // 2. Panel Rincian (GridBagLayout agar rapi)
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(CLR_BG_DARK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // -- Baris Judul --
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        JLabel lblHeader = new JLabel("RINCIAN PEMESANAN");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblHeader.setForeground(CLR_GOLD);
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        pnlForm.add(lblHeader, gbc);

        // -- Separator --
        gbc.gridy=1;
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.GRAY);
        pnlForm.add(sep, gbc);

        // -- Info --
        gbc.gridwidth=1;
        addFormRow(pnlForm, "Film", judul, 2);
        addFormRow(pnlForm, "Kursi", kursiSedangDipilih.size() + " pcs", 3);
        addFormRow(pnlForm, "Total Tagihan", rp.format(totalTagihan), 4);

        // -- Input Uang --
        gbc.gridx=0; gbc.gridy=5;
        JLabel lblInp = new JLabel("Uang Tunai (Rp)");
        lblInp.setForeground(Color.WHITE);
        pnlForm.add(lblInp, gbc);

        gbc.gridx=1;
        JTextField txtUang = new JTextField();
        txtUang.setFont(new Font("SansSerif", Font.BOLD, 14));
        txtUang.setBackground(CLR_BG_PANEL);
        txtUang.setForeground(Color.WHITE);
        txtUang.setCaretColor(CLR_GOLD);
        txtUang.setBorder(new CompoundBorder(new LineBorder(Color.GRAY), new EmptyBorder(5,5,5,5)));
        pnlForm.add(txtUang, gbc);

        // 3. Tombol Proses
        JButton btnProses = new JButton("BAYAR & CETAK TIKET");
        btnProses.setBackground(CLR_GOLD);
        btnProses.setForeground(Color.BLACK);
        btnProses.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnProses.setPreferredSize(new Dimension(0, 45));

        btnProses.addActionListener(ev -> {
            try {
                if (txtUang.getText().isEmpty()) return;
                int uangUser = Integer.parseInt(txtUang.getText());

                if (uangUser < totalTagihan) {
                    throw new PembayaranGagalException("Uang kurang " + rp.format(totalTagihan - uangUser));
                }

                // --- TRANSAKSI SUKSES ---
                int kembalian = uangUser - totalTagihan;

                // Simpan ke Database
                ArrayList<Integer> dataFilmIni = databaseKursi.get(judul);
                dataFilmIni.addAll(kursiSedangDipilih); // Tandai sebagai terjual

                dialog.dispose(); // Tutup Dialog Bayar

                // Tampilkan E-Receipt Proper
                tampilkanReceiptProper(judul, kursiSedangDipilih.size(), totalTagihan, uangUser, kembalian);

                // Reset UI Utama
                kursiSedangDipilih.clear();
                refreshTampilanKursi();

            } catch (PembayaranGagalException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Gagal", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Input uang harus angka tanpa titik!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(btnProses, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void addFormRow(JPanel p, String label, String val, int y) {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 10, 5, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx=0; g.gridy=y;
        JLabel l = new JLabel(label);
        l.setForeground(Color.GRAY);
        p.add(l, g);

        g.gridx=1;
        JLabel v = new JLabel(val);
        v.setForeground(Color.WHITE);
        v.setFont(new Font("SansSerif", Font.BOLD, 13));
        v.setHorizontalAlignment(SwingConstants.RIGHT);
        p.add(v, g);
    }

    // =================================================================
    // LOGIC: E-Receipt (Paper Style)
    // =================================================================
    private void tampilkanReceiptProper(String film, int qty, int total, int bayar, int kembali) {
        JDialog d = new JDialog(this, "E-Receipt", true);
        d.setSize(350, 500);
        d.setLocationRelativeTo(this);
        d.setLayout(new GridBagLayout()); // Center Alignment
        d.getContentPane().setBackground(new Color(20, 20, 20)); // Dark Background

        // Kertas Struk (Panel Putih)
        JPanel paper = new JPanel();
        paper.setPreferredSize(new Dimension(300, 420));
        paper.setBackground(new Color(245, 245, 245)); // Putih Kertas
        paper.setLayout(new BoxLayout(paper, BoxLayout.Y_AXIS));
        paper.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Isi Struk
        addReceiptLine(paper, "ROYAL CINEMA", 18, true);
        addReceiptLine(paper, "Premium Entertainment", 10, true);
        addReceiptLine(paper, "--------------------------------", 12, true);

        addReceiptLine(paper, "Film: " + film, 12, false);
        addReceiptLine(paper, "Date: " + java.time.LocalDate.now(), 12, false);
        addReceiptLine(paper, "--------------------------------", 12, true);

        addReceiptPair(paper, "Item Qty", qty + " Tiket");
        addReceiptPair(paper, "Price/Pcs", rp.format(total/qty));
        addReceiptLine(paper, " ", 5, false);
        addReceiptPair(paper, "TOTAL", rp.format(total));
        addReceiptLine(paper, "--------------------------------", 12, true);

        addReceiptPair(paper, "CASH", rp.format(bayar));
        addReceiptPair(paper, "CHANGE", rp.format(kembali));

        addReceiptLine(paper, "--------------------------------", 12, true);
        addReceiptLine(paper, "Thank you for visiting!", 10, true);
        addReceiptLine(paper, "Please keep this receipt.", 10, true);

        // Tambahkan Kertas ke Dialog
        d.add(paper);
        d.setVisible(true);
    }

    private void addReceiptLine(JPanel p, String text, int size, boolean center) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Monospaced", Font.BOLD, size));
        l.setForeground(Color.BLACK);
        l.setAlignmentX(center ? Component.CENTER_ALIGNMENT : Component.LEFT_ALIGNMENT);
        p.add(l);
        p.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void addReceiptPair(JPanel p, String left, String right) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(300, 20));

        JLabel l = new JLabel(left);
        l.setFont(new Font("Monospaced", Font.PLAIN, 12));
        l.setForeground(Color.BLACK);

        JLabel r = new JLabel(right);
        r.setFont(new Font("Monospaced", Font.BOLD, 12));
        r.setForeground(Color.BLACK);

        row.add(l, BorderLayout.WEST);
        row.add(r, BorderLayout.EAST);
        p.add(row);
        p.add(Box.createRigidArea(new Dimension(0, 5)));
    }


    // =================================================================
    // INNER CLASS: Kursi Custom (Bentuk Bioskop Normal)
    // =================================================================
    private class KursiButton extends JButton {
        boolean isSelected = false;
        boolean isSold = false;
        String label;

        public KursiButton(String label) {
            this.label = label;
            // UKURAN KURSI BIOSKOP NORMAL (Agak Tinggi, tidak lebar)
            setPreferredSize(new Dimension(45, 65));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public void resetStatus() { isSelected = false; isSold = false; }
        public void setSelected(boolean b) { isSelected = b; repaint(); }
        public void setSold(boolean b) { isSold = b; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color color = isSold ? SEAT_SOLD : (isSelected ? SEAT_SELECT : SEAT_AVAIL);
            g2.setColor(color);

            int w = getWidth();
            int h = getHeight();

            // 1. Sandaran Kepala (Headrest) - Kecil di atas
            g2.fillRoundRect(5, 2, w-10, 15, 8, 8);

            // 2. Sandaran Punggung (Backrest) - Utama
            g2.fillRoundRect(2, 20, w-4, 25, 8, 8);

            // 3. Dudukan (Seat Base) - Bawah
            g2.fillRoundRect(2, 48, w-4, 15, 8, 8);

            // 4. Sandaran Tangan (Armrest) - Kiri Kanan
            g2.setColor(color.darker());
            g2.fillRoundRect(0, 30, 6, 30, 4, 4); // Kiri
            g2.fillRoundRect(w-6, 30, 6, 30, 4, 4); // Kanan

            // Nomor Kursi
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(label)) / 2;
            g2.drawString(label, tx, 36); // Posisi text di tengah sandaran punggung

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AplikasiBioskop().setVisible(true));
    }
}