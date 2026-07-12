/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import GUI.Panel.Settings;
import java.util.Date;
import java.util.Locale;
import javax.swing.Timer;
import Services.MahasiswaService;
import Services.I18nService;
import Objects.Mahasiswa;
import Util.SecurityUtils;
import Util.EncryptionUtils;
import javax.swing.JOptionPane;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import Util.MongoManager;
import java.awt.Frame;
import javax.swing.SwingUtilities;

/**
 *
 * @author user
 */
// 1. TAMBAHKAN IMPLEMENTS I18nChangeListener
public class AttendancePage extends javax.swing.JFrame implements I18nService.I18nChangeListener {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AttendancePage.class.getName());

    // TAMBAHAN: Variabel untuk thread delay jLabel2
    private Thread delayThread;

    /**
     * Creates new form AttendancePage
     */
    public AttendancePage() {
        I18nService.setLocale(Locale.of(Settings.prefs.get("LANGUAGE", Settings.statusLang)));
        initComponents(); // Kode bawaan NetBeans untuk inisialisasi komponen GUI
        
        // 2. DAFTARKAN FRAME INI SEBAGAI LISTENER BAHASA
        I18nService.registerListener(this);
        
        // TAMBAHAN: Set teks awal jLabel2 agar dinamis membaca bahasa
        jLabel2.setText(getLocalizedStatus());
        
        // Update teks UI awal sesuai bahasa yang terpilih
        updateUIText();
        
        // --- KODE REAL-TIME UNTUK TANGGAL DAN JAM ---
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mengambil waktu saat ini
                Date waktuSekarang = new Date();
                
                // 3. GUNAKAN LOCALE DARI I18NSERVICE AGAR NAMA HARI/BULAN MENYESUAIKAN BAHASA
                Locale currentLocale = I18nService.getCurrentLocale();
                if (currentLocale == null) {
                    currentLocale = Locale.of("id");
                }
                
                SimpleDateFormat formatTanggal = new SimpleDateFormat("EEEE, dd MMMM yyyy", currentLocale);
                SimpleDateFormat formatJam = new SimpleDateFormat("HH:mm:ss");
                
                // Terapkan hasil format ke masing-masing label
                lbltgl.setText(formatTanggal.format(waktuSekarang));
                lbljam.setText(formatJam.format(waktuSekarang));
            }
        });
        
        // Menjalankan timer
        timer.start();
        // --------------------------------------------
        
        showTapCardState();
    }
    
    // TAMBAHAN: Method ini untuk memastikan teks Masuk/Pulang selalu mengikuti bahasa yang aktif
    private String getLocalizedStatus() {
        String status = Settings.prefs.get("LAST_STATUS", "Masuk");
        return I18nService.get("ui.status." + status.toLowerCase());
    }

    // 4. METHOD WAJIB DARI INTERFACE I18N
    @Override
    public void onLanguageChanged() {
        SwingUtilities.invokeLater(() -> {
            updateUIText();
            // TAMBAHAN: Refresh tulisan Masuk/Pulang di jLabel2 jika tidak sedang muncul pesan delay
            if (delayThread == null || !delayThread.isAlive()) {
                jLabel2.setText(getLocalizedStatus());
            }
        });
    }
    
    // 5. METHOD UNTUK UPDATE TEKS KOMPONEN STATIS
    private void updateUIText() {
        jLabel1.setText(I18nService.get("ui.attendance.now"));
        masuk.setText(I18nService.get("ui.attendance.btn_in"));
        jButton1.setText(I18nService.get("ui.attendance.btn_back"));
        
        this.revalidate();
        this.repaint();
    }
    
    // TAMBAHAN: Method delay untuk menahan notifikasi sukses/gagal selama 3 detik di jLabel2
    private void updateLabelWithDelay(javax.swing.JLabel comp, String info) {
        if (delayThread != null && delayThread.isAlive()) {
            delayThread.interrupt();
        }

        if (info == null || info.isEmpty()) {
            comp.setText(getLocalizedStatus());
            return;
        }

        delayThread = new Thread(() -> {
            SwingUtilities.invokeLater(() -> comp.setText(info));
            try {
                Thread.sleep(3000);
                String statusDefault = getLocalizedStatus();
                SwingUtilities.invokeLater(() -> comp.setText(statusDefault));
            } catch (InterruptedException e) {
                // Diabaikan jika thread diinterupsi
            }
        });
        
        delayThread.setName("delayThread_jLabel2"); 
        delayThread.setDaemon(true);         
        delayThread.start();
    }

    /**
     * Mengembalikan kondisi UI ke mode siap menerima tap kartu (Standby)
     */
    private void showTapCardState() {
        lbluid.setText(""); // Bersihkan text field UID
        masuk.setVisible(false); // Sembunyikan tombol manual karena proses otomatis
        
        // Reset total tampilan panel tengah menggunakan KEY I18N
        lblnamabsr.setText("");
        lblnim.setText("");
        lblkelas.setText("");
        
        // Pasang kembali ikon default lingkaran toska
        try {
            lblicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tap.png"))); 
        } catch(Exception e){}
        lblicon.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lbltgl = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lbljam = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        masuk = new javax.swing.JButton();
        lbluid = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblnim = new javax.swing.JLabel();
        lblkelas = new javax.swing.JLabel();
        lblnamabsr = new javax.swing.JLabel();
        lblicon = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(0, 204, 204));

        lbltgl.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lbltgl.setForeground(new java.awt.Color(255, 255, 255));
        lbltgl.setText("Tanggal");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbltgl, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(lbltgl)
                .addGap(16, 16, 16))
        );

        jPanel3.setBackground(new java.awt.Color(0, 204, 204));

        lbljam.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lbljam.setForeground(new java.awt.Color(255, 255, 255));
        lbljam.setText("waktu");

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("SEKARANG");

        masuk.setText("Masuk");
        masuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masukActionPerformed(evt);
            }
        });

        lbluid.setText("UID");
        lbluid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbluidActionPerformed(evt);
            }
        });

        jButton1.setText("Kembali");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(lbljam, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(masuk, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbluid)))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbljam, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbluid, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(masuk, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setBackground(new java.awt.Color(89, 86, 83));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("jLabel2");
        jLabel2.setOpaque(true);

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        lblnim.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblnim.setForeground(new java.awt.Color(255, 255, 255));
        lblnim.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblnim.setText("jLabel3");

        lblkelas.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblkelas.setForeground(new java.awt.Color(255, 255, 255));
        lblkelas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblkelas.setText("jLabel3");

        lblnamabsr.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblnamabsr.setForeground(new java.awt.Color(255, 255, 255));
        lblnamabsr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblnamabsr.setText("<Code>");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(115, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblnamabsr)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addComponent(lblkelas, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(43, 43, 43))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addComponent(lblnim, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap()))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblnamabsr)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblnim)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblkelas)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        lblicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tap.png"))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblicon)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblicon)
                .addGap(66, 66, 66)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(35, 66, 31, 65);
        jPanel5.add(jPanel6, gridBagConstraints);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void masukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masukActionPerformed
        String namaMhs = lblnamabsr.getText().trim();
        String nimMhs = lblnim.getText().trim();
        
        // Pengecekan dinamis bahasa untuk penggantian kata "Kelas: " atau "Class: "
        String prefixKelas = I18nService.get("ui.attendance.class");
        String kelasMhs = lblkelas.getText().replace(prefixKelas, "").trim();

        // Validasi apakah form masih kosong (belum tap kartu)
        if (nimMhs.isEmpty() || namaMhs.isEmpty() || namaMhs.equals(I18nService.get("ui.attendance.tap_card_title"))) {
            return; 
        }

        try {
            // Menangkap waktu penekanan tombol log absen
            Date sekarang = new Date();
            SimpleDateFormat formatTanggal = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatJam = new SimpleDateFormat("HH:mm:ss");
            
            String tanggalAbsen = formatTanggal.format(sekarang);
            String jamAbsen = formatJam.format(sekarang);

            // 1. UPDATE PANEL TENGAH MENJADI NOTIFIKASI BERHASIL MASUK MENGGUNAKAN I18N
            
            // TAMBAHAN: Update text jLabel2 menjadi sukses saat proses berhasil!
            updateLabelWithDelay(jLabel2, I18nService.get("ui.status.success"));

            // 2. SIMPAN LOG ABSENSI KE MONGODB
            MongoDatabase database = MongoManager.getDatabase();
            MongoCollection<Document> collection = database.getCollection("Absensi_log");

            // Membuat struktur document data (NIM asli disimpan sebagai teks biasa)
            Document logAbsen = new Document()
                    .append("nim", nimMhs)
                    .append("nama", namaMhs)
                    .append("kelas", kelasMhs)
                    .append("tanggal", tanggalAbsen)
                    .append("jam", jamAbsen);

            // Eksekusi simpan data ke MongoDB
            collection.insertOne(logAbsen);
            
            Timer delayReset = new Timer(3000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showTapCardState(); 
                    lbluid.requestFocus(); 
                }
            });
            
            delayReset.setRepeats(false); 
            delayReset.start(); 
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mencatat kehadiran ke Database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_masukActionPerformed

    private void lbluidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbluidActionPerformed
 String uidMentah = lbluid.getText().trim().toLowerCase(); 
        
        if (!uidMentah.isEmpty()) {
            try {
                MahasiswaService service = new MahasiswaService();
                String uidHashed = SecurityUtils.getHash(uidMentah, SecurityUtils.SHA_256).trim().toLowerCase();
                
                Mahasiswa mhs = service.findByUid(uidHashed); 
                
                if (mhs != null) {
                    String nimAsli = EncryptionUtils.decrypt(mhs.getNimMahasiswa());
                    // Ambil waktu realtime saat kartu menyentuh mesin reader

                    
                    // 1. Tampilkan Informasi Mahasiswa Menggunakan I18N
                    
                    lblnamabsr.setText(mhs.getNamaLengkap());
                    lblnim.setText(nimAsli);
                    lblkelas.setText(I18nService.get("ui.attendance.class") + mhs.getKelas());
                    
                    try {
                        lblicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/masuk.png")));
                    } catch (Exception ex) {}

                    // Baris ini akan mengeksekusi method masukActionPerformed, yang di dalamnya sudah ada updateLabelWithDelay sukses
                    masukActionPerformed(null);

                } else {
                    // TAMBAHAN: Munculkan teks gagal pada jLabel2
                    updateLabelWithDelay(jLabel2, I18nService.get("ui.status.failed"));

//                    // Peringatan kartu tidak terdaftar menggunakan I18N
//                    JOptionPane.showMessageDialog(this, I18nService.get("ui.attendance.unregistered"), "Peringatan", JOptionPane.WARNING_MESSAGE);
                    showTapCardState();
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal membaca kartu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                showTapCardState();
            }
        }
    }//GEN-LAST:event_lbluidActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       AdminPage adm = new AdminPage();
       adm.setVisible(true);
       adm.setExtendedState(Frame.MAXIMIZED_BOTH); 
       this.dispose(); // Optional: tutup halaman attendance saat kembali ke admin 
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new AttendancePage().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lblicon;
    private javax.swing.JLabel lbljam;
    private javax.swing.JLabel lblkelas;
    private javax.swing.JLabel lblnamabsr;
    private javax.swing.JLabel lblnim;
    private javax.swing.JLabel lbltgl;
    private javax.swing.JTextField lbluid;
    private javax.swing.JButton masuk;
    // End of variables declaration//GEN-END:variables
}
