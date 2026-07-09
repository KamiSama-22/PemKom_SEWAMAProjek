package PALETTE;

import Services.I18nService;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.Beans; // IMPORT PENTING UNTUK GUI BUILDER
import javax.swing.JToggleButton;

/**
 * Komponen SlidingStatusToggle
 * Custom toggle button untuk mengubah status secara visual dengan efek geser.
 */
public class SlidingStatusToggle extends JToggleButton {

    // Tema warna dark-mode disesuaikan dengan Bootstrap modern
    private final Color COLOR_BG = new Color(39, 45, 54);            // Background wadah abu-abu gelap
    private final Color COLOR_SLIDER_MASUK = new Color(25, 135, 84); // Slider Hijau (Masuk - Kiri)
    private final Color COLOR_SLIDER_PULANG = new Color(220, 53, 69);  // Slider Merah (Pulang - Kanan)

    private final int cornerRadius = 24; // Membuat bentuk kapsul/pil yang halus

    public SlidingStatusToggle() {
        super();

        // Matikan render bawaan agar tidak merusak custom painting kita
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("SansSerif", Font.BOLD, 14));

        // Memicu gambar ulang (repaint) setiap kali status tombol berubah klik
        addActionListener(e -> repaint());
    }

    /**
     * Mengubah status toggle berdasarkan variabel String ("Masuk" / "Pulang")
     * Biasanya dipanggil saat inisialisasi / muat ulang halaman.
     * @param status
     */
    public void setStatusByString(String status) {
        if (Beans.isDesignTime()) {
            this.setSelected("Pulang".equalsIgnoreCase(status));
            repaint();
            return;
        }

        // Kode asli saat program di-Run
        try {
            if (I18nService.get("ui.status.out") != null && 
                I18nService.get("ui.status.out").equalsIgnoreCase(status)) {
                this.setSelected(true);  // Geser ke Kanan (Pulang)
            } else {
                this.setSelected(false); // Geser ke Kiri (Masuk)
            }
        } catch (Throwable t) {
            this.setSelected("Pulang".equalsIgnoreCase(status));
        }
        repaint(); // Gambar ulang posisi slider
    }

    /**
     * Mengambil status saat ini dalam bentuk String
     * @return 
     */
    public String getStatusString() {
        if (Beans.isDesignTime()) {
            return isSelected() ? "Pulang" : "Masuk";
        }

        try {
            String statusOut = I18nService.get("ui.status.out");
            String statusIn = I18nService.get("ui.status.in");
            return isSelected() ? (statusOut != null ? statusOut : "Pulang") 
                                : (statusIn != null ? statusIn : "Masuk");  
        } catch (Throwable t) {
            return isSelected() ? "Pulang" : "Masuk";
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Mengaktifkan anti-aliasing agar teks dan sudut membulat tidak pecah
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int margin = 5; // Jarak/Padding antara slider dalam dengan pembungkus luar
        int sliderWidth = (w / 2) - margin;
        int sliderHeight = h - (margin * 2);

        // Status penentu: false = Kiri (Masuk), true = Kanan (Pulang)
        boolean isPulangActive = isSelected();

        // 1. GAMBAR BACKGROUND UTAMA (Kapsul Abu-abu)
        g2.setColor(COLOR_BG);
        g2.fillRoundRect(0, 0, w, h, cornerRadius, cornerRadius);

        // 2. GAMBAR SLIDER HIT (Kotak Indikator Bergeser)
        int sliderX = isPulangActive ? (w / 2) : margin;
        g2.setColor(isPulangActive ? COLOR_SLIDER_PULANG : COLOR_SLIDER_MASUK);
        g2.fillRoundRect(sliderX, margin, sliderWidth, sliderHeight, cornerRadius - 6, cornerRadius - 6);

        // 3. SIAPKAN TEKS (Nilai Default agar IDE NetBeans tidak crash)
        String textLeft = "Masuk";
        String textRight = "Pulang";

        // MENCEGAH CRASH: Hanya panggil I18nService jika BUKAN di NetBeans Design Mode
        if (!Beans.isDesignTime()) {
            try {
                String tempLeft = I18nService.get("ui.status.in");
                if (tempLeft != null) textLeft = tempLeft;

                String tempRight = I18nService.get("ui.status.out");
                if (tempRight != null) textRight = tempRight;
            } catch (Throwable t) {
                // Tangkap NoClassDefFoundError / Exception lain agar GUI Builder tetap aman
            }
        }

        // 4. TATA LETAK TEKS ("Masuk" di Kiri & "Pulang" di Kanan)
        FontMetrics fm = g2.getFontMetrics();
        int textY = (h / 2) + (fm.getAscent() / 2) - 2; // Sumbu Y presisi di tengah vertikal

        // Gambar Teks Sisi Kiri
        int textLeftX = (w / 4) - (fm.stringWidth(textLeft) / 2); 
        g2.setColor(!isPulangActive ? Color.WHITE : new Color(130, 135, 145));
        g2.drawString(textLeft, textLeftX, textY);

        // Gambar Teks Sisi Kanan
        int textRightX = ((w / 4) * 3) - (fm.stringWidth(textRight) / 2); 
        g2.setColor(isPulangActive ? Color.WHITE : new Color(130, 135, 145));
        g2.drawString(textRight, textRightX, textY);

        g2.dispose();
    }
}