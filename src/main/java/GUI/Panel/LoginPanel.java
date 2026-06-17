/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Panel;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author ADVAN
 */
public class LoginPanel extends JPanel {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginPanel() {
        // Menggunakan null layout agar penempatan komponen bisa presisi sesuai gambar
        setLayout(null);
        setOpaque(false); // Agar background rounded buatan kita terlihat jelas
        setPreferredSize(new Dimension(600, 450)); // Menentukan ukuran default kotak putih

        // --- 1. Text Field Pertama (Username) ---
        txtUsername = new RoundedTextField(20);
        txtUsername.setBounds(75, 110, 450, 50);
        txtUsername.setBackground(new Color(238, 238, 238));
        txtUsername.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); // Padding teks di dalam
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(txtUsername);

        // --- 2. Text Field Kedua (Password) ---
        txtPassword = new RoundedPasswordField(20);
        txtPassword.setBounds(75, 180, 450, 50);
        txtPassword.setBackground(new Color(238, 238, 238));
        txtPassword.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); // Padding teks di dalam
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(txtPassword);

        // --- 3. Tombol Login ---
        btnLogin = new JButton("Login");
        btnLogin.setBounds(225, 260, 150, 35);
        btnLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnLogin.setFocusPainted(false);
        add(btnLogin);

        // --- 4. Teks Hak Cipta ---
        JLabel lblCopyright = new JLabel("© 2026 Universitas Harkat Negeri", SwingConstants.CENTER);
        lblCopyright.setBounds(0, 400, 600, 30);
        lblCopyright.setForeground(new Color(180, 180, 180));
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(lblCopyright);
    }

    // Menggambar background kotak putih dengan sudut melengkung (rounded)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(Color.WHITE);
        // Menggambar kotak putih melengkung (arcWidth: 30, arcHeight: 30)
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
        
        g2.dispose();
    }

    // --- KELAS PEMBANTU: Membuat JTextField berbentuk bulat (Rounded) ---
    class RoundedTextField extends JTextField {
        private int radius;
        public RoundedTextField(int radius) {
            this.radius = radius;
            setOpaque(false);
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    // --- KELAS PEMBANTU: Membuat JPasswordField berbentuk bulat (Rounded) ---
    class RoundedPasswordField extends JPasswordField {
        private int radius;
        public RoundedPasswordField(int radius) {
            this.radius = radius;
            setOpaque(false);
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            super.paintComponent(g2);
            g2.dispose();
        }
    }
}