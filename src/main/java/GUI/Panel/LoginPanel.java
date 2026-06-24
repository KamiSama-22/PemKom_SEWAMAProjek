/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Panel;

import GUI.LoginPage;
import Services.AuthService;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
//import GUI.AdminPage;
//import java.awt.BorderLayout;

/**
 *
 * @author ADVAN
 */
public class LoginPanel extends JPanel {

    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JButton btnLogin;

    public LoginPanel() {
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(600, 450));

        // --- 1. Text Field Username ---
        txtUsername = new RoundedTextField(20);
        txtUsername.setBounds(75, 110, 450, 50);
        txtUsername.setBackground(new Color(238, 238, 238));
        txtUsername.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(txtUsername);

        // --- 2. Text Field Password ---
        txtPassword = new RoundedPasswordField(20);
        txtPassword.setBounds(75, 180, 450, 50);
        txtPassword.setBackground(new Color(238, 238, 238));
        txtPassword.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(txtPassword);

        // Event Enter pada username dipindahkan ke sini
        // karena txtPassword sekarang sudah dibuat
        txtUsername.addActionListener(e -> txtPassword.requestFocus());

        // Jika tekan Enter di password, langsung login
        txtPassword.addActionListener(e -> doLogin());

        // --- 3. Tombol Login ---
        btnLogin = new JButton("Login");
        btnLogin.setBounds(225, 260, 150, 35);
        btnLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnLogin);

        // Jika tombol login diklik
        btnLogin.addActionListener(e -> doLogin());

        // --- 4. Teks Hak Cipta ---
        JLabel lblCopyright = new JLabel("© 2026 Universitas Harkat Negeri", SwingConstants.CENTER);
        lblCopyright.setBounds(0, 400, 600, 30);
        lblCopyright.setForeground(new Color(180, 180, 180));
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(lblCopyright);
    }

    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon isi Username Anda");
            txtUsername.requestFocus();
        } else if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon isi Password Anda");
            txtPassword.requestFocus();
        } else {
            AuthService userService = new AuthService();

            /*
             * Karena LoginPanel adalah JPanel yang ditempel di LoginPage,
             * maka ambil JFrame induknya lalu ubah menjadi LoginPage.
             */
            Window window = SwingUtilities.getWindowAncestor(this);

            if (window instanceof LoginPage) {
                LoginPage loginPage = (LoginPage) window;
                userService.login(username, password, loginPage);
            } else {
                JOptionPane.showMessageDialog(this, "LoginPage utama tidak ditemukan");
            }
        }
    }

    // Menggambar background kotak putih dengan sudut melengkung
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));

        g2.dispose();
    }

    // --- KELAS PEMBANTU: Membuat JTextField berbentuk bulat ---
    class RoundedTextField extends JTextField {

        private final int radius;

        public RoundedTextField(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            super.paintComponent(g2);
            g2.dispose();
        }
    }

    // --- KELAS PEMBANTU: Membuat JPasswordField berbentuk bulat ---
    class RoundedPasswordField extends JPasswordField {

        private final int radius;

        public RoundedPasswordField(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            super.paintComponent(g2);
            g2.dispose();
        }
    }
}