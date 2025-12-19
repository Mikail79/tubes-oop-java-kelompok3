package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class AplikasiBioskop extends JFrame {

    // --- PALETTE WARNA ---
    public static final Color CLR_DARK = new Color(15, 23, 42);
    public static final Color CLR_PRIMARY = new Color(30, 41, 59);
    public static final Color CLR_GOLD = new Color(212, 175, 55);
    public static final Color CLR_WHITE = new Color(241, 245, 249);

    // Data App
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    private String currentUser = "Guest";
    private String selectedFilm = "";
    private int selectedPrice = 0;
    private ArrayList<Integer> currentSeats = new ArrayList<>();
    private HashMap<String, ArrayList<Integer>> soldSeatsDB = new HashMap<>();

    // Panels
    private LoginPanel loginPanel;
    private PaymentPanel paymentPanel;
    private ReceiptPanel receiptPanel;
    private SeatPanel seatPanel;
    private LandingPanel landingPanel;

    // Data Film
    private Object[][] filmsData = {
            {"Avatar: The Way of Water", 65000, "Sci-Fi", "⭐ 4.8", "avatar.jpg"},
            {"Spider-Man: No Way Home", 50000, "Action", "⭐ 4.9", "spiderman.jpg"},
            {"Oppenheimer", 55000, "Drama", "⭐ 4.7", "oppenheimer.jpg"},
            {"Barbie", 45000, "Comedy", "⭐ 4.5", "barbie.png"},
            {"Mission Impossible 7", 60000, "Action", "⭐ 4.6", "mission.jpg"},
            {"The Batman", 55000, "Crime", "⭐ 4.7", "batman.jpg"},
            {"Interstellar", 50000, "Sci-Fi", "⭐ 4.9", "interstellar.jpg"},
            {"Inception", 50000, "Sci-Fi", "⭐ 4.8", "inception.jpg"}
    };

    public AplikasiBioskop() {
        setTitle("Cinemaxx Premier");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Init DB
        for(Object[] f : filmsData) soldSeatsDB.put((String)f[0], new ArrayList<>());

        // Init Panels
        loginPanel = new LoginPanel(this);
        paymentPanel = new PaymentPanel(this);
        receiptPanel = new ReceiptPanel(this);
        seatPanel = new SeatPanel(this);
        landingPanel = new LandingPanel();

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(landingPanel, "LANDING");
        mainPanel.add(seatPanel, "SEAT");
        mainPanel.add(paymentPanel, "PAYMENT");
        mainPanel.add(receiptPanel, "RECEIPT");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
    }

    // --- LOGIC UTAMA: KONVERSI NOMOR KURSI ---
    // Ini adalah 'Otak' penamaan kursi. Semua panel wajib pakai ini.
    public String getSeatName(int index) {
        // Grid kita 6 Kolom (A1..A6, B1..B6)
        char baris = (char) ('A' + (index / 6)); // 0-5 -> A, 6-11 -> B
        int nomor = (index % 6) + 1;             // 0 -> 1, 1 -> 2
        return "" + baris + nomor;
    }

    // --- API Methods ---
    public void showPage(String page) {
        if(page.equals("LANDING")) landingPanel.updateUser();
        cardLayout.show(mainPanel, page);
    }
    public void setCurrentUser(String name) { this.currentUser = name; }
    public String getCurrentUser() { return currentUser; }
    public String getSelectedFilm() { return selectedFilm; }
    public int getSelectedPrice() { return selectedPrice; }
    public ArrayList<Integer> getCurrentSeats() { return currentSeats; }
    public PaymentPanel getPaymentPanel() { return paymentPanel; }
    public ReceiptPanel getReceiptPanel() { return receiptPanel; }
    public void addSoldSeats(String film, ArrayList<Integer> seats) { soldSeatsDB.get(film).addAll(seats); }
    public ArrayList<Integer> getSoldSeats(String film) { return soldSeatsDB.get(film); }
    public void resetSelection() { currentSeats.clear(); }

    // =================================================================
    // LANDING PANEL
    // =================================================================
    private class LandingPanel extends JPanel {
        private JLabel lblUser;
        private JPanel carouselContainer;
        private JScrollPane scrollPane;

        public LandingPanel() {
            setLayout(new BorderLayout());
            setBackground(CLR_DARK);

            JPanel navbar = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    LinearGradientPaint lgp = new LinearGradientPaint(
                            new Point(0, 0), new Point(getWidth(), 0),
                            new float[]{0f, 0.5f, 1f},
                            new Color[]{new Color(5, 5, 10), CLR_PRIMARY, new Color(5, 5, 10)}
                    );
                    g2.setPaint(lgp);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(new Color(212, 175, 55, 100));
                    g2.fillRect(0, getHeight()-2, getWidth(), 2);
                }
            };
            navbar.setPreferredSize(new Dimension(0, 80));
            navbar.setBorder(new EmptyBorder(0, 40, 0, 40));

            JLabel logo = new JLabel("CINEMAXX");
            logo.setFont(new Font("Cinzel", Font.BOLD, 28));
            logo.setForeground(CLR_GOLD);

            lblUser = new JLabel("Welcome");
            lblUser.setForeground(CLR_WHITE);
            lblUser.setFont(new Font("SansSerif", Font.BOLD, 14));

            RoundedButton btnLogout = new RoundedButton("Logout");
            btnLogout.setBackgroundColor(new Color(180, 50, 50));
            btnLogout.setPreferredSize(new Dimension(90, 35));
            btnLogout.addActionListener(e -> showPage("LOGIN"));

            JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 22));
            rightNav.setOpaque(false);
            rightNav.add(lblUser);
            rightNav.add(btnLogout);

            navbar.add(logo, BorderLayout.WEST);
            navbar.add(rightNav, BorderLayout.EAST);

            // HERO BANNER
            JPanel heroPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    GradientPaint gp = new GradientPaint(0, 0, CLR_PRIMARY, getWidth(), getHeight(), CLR_DARK);
                    g2.setPaint(gp);
                    g2.fillRoundRect(40, 10, getWidth()-80, getHeight()-20, 30, 30);
                }
            };
            heroPanel.setPreferredSize(new Dimension(0, 300));
            heroPanel.setLayout(null);

            JLabel heroTitle = new JLabel("AVATAR: THE WAY OF WATER");
            heroTitle.setFont(new Font("Cinzel", Font.BOLD, 36));
            heroTitle.setForeground(CLR_WHITE);
            heroTitle.setBounds(100, 70, 600, 40);

            JLabel heroSub = new JLabel("<html>Return to Pandora. Experience the new cinematic masterpiece<br>in stunning IMAX 3D laser projection.</html>");
            heroSub.setFont(new Font("SansSerif", Font.PLAIN, 16));
            heroSub.setForeground(new Color(200, 200, 200));
            heroSub.setBounds(100, 120, 600, 50);

            RoundedButton btnPromo = new RoundedButton("BOOK NOW");
            btnPromo.setBackgroundColor(CLR_GOLD);
            btnPromo.setForeground(Color.BLACK);
            btnPromo.setBounds(100, 200, 160, 50);
            btnPromo.addActionListener(e -> {
                selectedFilm = "Avatar: The Way of Water";
                selectedPrice = 65000;
                ((SeatPanel)mainPanel.getComponent(2)).refreshSeats();
                showPage("SEAT");
            });

            heroPanel.add(heroTitle); heroPanel.add(heroSub); heroPanel.add(btnPromo);

            // CAROUSEL
            JPanel contentWrapper = new JPanel(new BorderLayout());
            contentWrapper.setOpaque(false);
            contentWrapper.setBorder(new EmptyBorder(20, 40, 20, 40));

            JLabel lblSection = new JLabel("Now Playing");
            lblSection.setFont(new Font("SansSerif", Font.BOLD, 22));
            lblSection.setForeground(CLR_GOLD);
            lblSection.setBorder(new EmptyBorder(0, 10, 15, 0));

            carouselContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 10));
            carouselContainer.setBackground(CLR_DARK);

            for(Object[] f : filmsData) {
                carouselContainer.add(createMovieCard(
                        (String)f[0], (int)f[1], (String)f[2], (String)f[3], (String)f[4]
                ));
            }

            scrollPane = new JScrollPane(carouselContainer);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(null);
            scrollPane.getViewport().setBackground(CLR_DARK);

            JButton btnLeft = createNavButton("❮");
            JButton btnRight = createNavButton("❯");
            btnLeft.addActionListener(e -> scrollSmoothly(-350));
            btnRight.addActionListener(e -> scrollSmoothly(350));

            JPanel carouselWrapper = new JPanel(new BorderLayout());
            carouselWrapper.setOpaque(false);
            carouselWrapper.add(btnLeft, BorderLayout.WEST);
            carouselWrapper.add(scrollPane, BorderLayout.CENTER);
            carouselWrapper.add(btnRight, BorderLayout.EAST);

            contentWrapper.add(lblSection, BorderLayout.NORTH);
            contentWrapper.add(carouselWrapper, BorderLayout.CENTER);

            JPanel mainContent = new JPanel(new BorderLayout());
            mainContent.setOpaque(false);
            mainContent.add(heroPanel, BorderLayout.NORTH);
            mainContent.add(contentWrapper, BorderLayout.CENTER);

            add(navbar, BorderLayout.NORTH);
            add(mainContent, BorderLayout.CENTER);
        }

        private void scrollSmoothly(int delta) {
            JScrollBar bar = scrollPane.getHorizontalScrollBar();
            bar.setValue(bar.getValue() + delta);
        }

        private JButton createNavButton(String txt) {
            JButton btn = new JButton(txt);
            btn.setFont(new Font("SansSerif", Font.BOLD, 28));
            btn.setForeground(CLR_GOLD);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return btn;
        }

        public void updateUser() { lblUser.setText("Hi, " + currentUser); }

        private JPanel createMovieCard(String title, int price, String genre, String rating, String imgFileName) {
            JPanel card = new JPanel();
            card.setPreferredSize(new Dimension(190, 300));
            card.setLayout(new BorderLayout());
            card.setBackground(CLR_PRIMARY);

            JLabel posterLabel = new JLabel();
            posterLabel.setPreferredSize(new Dimension(190, 200));
            posterLabel.setHorizontalAlignment(SwingConstants.CENTER);

            try {
                ImageIcon icon = new ImageIcon("resources/" + imgFileName);
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE && icon.getIconWidth() > 0) {
                    Image img = icon.getImage().getScaledInstance(190, 200, Image.SCALE_SMOOTH);
                    posterLabel.setIcon(new ImageIcon(img));
                } else {
                    posterLabel.setText("🎬");
                    posterLabel.setForeground(Color.GRAY);
                    posterLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
                }
            } catch (Exception e) {
                posterLabel.setText("Error");
            }

            JPanel info = new JPanel(new GridLayout(3, 1));
            info.setOpaque(false);
            info.setBorder(new EmptyBorder(8, 10, 8, 10));

            JLabel lTitle = new JLabel(title);
            lTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
            lTitle.setForeground(CLR_WHITE);

            JLabel lMeta = new JLabel(genre + " | " + rating);
            lMeta.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lMeta.setForeground(Color.LIGHT_GRAY);

            info.add(lTitle); info.add(lMeta);

            RoundedButton btnBook = new RoundedButton("IDR " + (price/1000) + "k");
            btnBook.setFont(new Font("SansSerif", Font.BOLD, 12));
            btnBook.setPreferredSize(new Dimension(0, 32));
            btnBook.addActionListener(e -> {
                selectedFilm = title;
                selectedPrice = price;
                ((SeatPanel)mainPanel.getComponent(2)).refreshSeats();
                showPage("SEAT");
            });

            card.add(posterLabel, BorderLayout.NORTH);
            card.add(info, BorderLayout.CENTER);
            card.add(btnBook, BorderLayout.SOUTH);
            return card;
        }
    }

    public static class RoundedButton extends JButton {
        private Color bgColor = CLR_PRIMARY;
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if(bgColor == CLR_GOLD) setForeground(Color.WHITE);
                    else setForeground(CLR_GOLD);
                }
                public void mouseExited(MouseEvent e) {
                    if(bgColor == CLR_GOLD) setForeground(CLR_PRIMARY);
                    else setForeground(Color.WHITE);
                }
            });
        }
        public void setBackgroundColor(Color c) { this.bgColor = c; repaint(); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AplikasiBioskop().setVisible(true));
    }
}