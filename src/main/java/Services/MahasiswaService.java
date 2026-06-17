/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import GUI.Panel.MahasiswaPanel;
import DAO.GenericDAO;
import Objects.Mahasiswa;
import com.mongodb.client.model.Filters;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.bson.conversions.Bson;

/**
 *
 * @author mnish
 */
public class MahasiswaService {

    // Inisialisasi GenericDAO khusus untuk entitas Mahasiswa
    // Menggunakan koleksi "mahasiswa" dan referensi Class Mahasiswa [3]
    private final GenericDAO<Mahasiswa> DAO;

    public MahasiswaService() {
        this.DAO = new GenericDAO<>("mahasiswa", Mahasiswa.class);
    }

    /**
     * 1.CREATE: Fungsi untuk menyimpan data mahasiswa baru ke MongoDB [2], [3]
     *
     * @param mahasiswaBaru
     */
    public void tambahMahasiswa(Mahasiswa mahasiswaBaru) {
    try {
        // 1. Amankan UID RFID menggunakan Hashing SHA-256 dari SecurityUtils
        String uidMentah = mahasiswaBaru.getUidRfid();
        String uidHashed = Util.SecurityUtils.getHash(uidMentah, Util.SecurityUtils.SHA_256);
        mahasiswaBaru.setUidRfid(uidHashed);
        
        // 2. Amankan NIM menggunakan Enkripsi AES Dua Arah
        String nimMentah = mahasiswaBaru.getNimMahasiswa();
        String nimTerenskripsi = Util.EncryptionUtils.encrypt(nimMentah);
        mahasiswaBaru.setNimMahasiswa(nimTerenskripsi);
        
        // 3. Simpan objek yang sudah aman ke MongoDB melalui GenericDAO
        DAO.save(mahasiswaBaru);
        
    } catch (Exception e) {
        System.err.println("Gagal mengamankan atau menyimpan data: " + e.getMessage());
        e.printStackTrace();
    }
}

    public void tambahMahasiswa(String uidRfid, String nimMahasiswa, String namaLengkap, String kelas) {
        Mahasiswa mahasiswaBaru = new Mahasiswa(uidRfid, nimMahasiswa, namaLengkap, kelas);
        DAO.save(mahasiswaBaru); // Memanggil insertOne melalui GenericDAO [3]
    }

    /**
     * 2. READ (All): Fungsi untuk mengambil semua data mahasiswa [5], [6]
     */
    public void tampilkanDaftarMahasiswa() {
        List<Mahasiswa> daftar = DAO.findAll();
        System.out.println("--- Daftar Mahasiswa ---");
        for (Mahasiswa m : daftar) {
            System.out.println(m.toString()); // Menggunakan format toString di sumber [7]
        }
    }

    /**
     * 2.READ (All): Fungsi untuk mengambil semua data mahasiswa [5], [6]
     *
     * @param panelTarget
     * @param key
     */
    public void tampilMahasiswa(JPanel panelTarget, String key) {
        //1. 
        // Menampilkan data berdasarkan request
        // key "null/kosong" = get all data
        // key "filled" = get specific data

        List<Mahasiswa> daftarMahasiswa;
        if (key.isEmpty()) {
            //Mengambil data dari database menggunakan GenericDAO
            daftarMahasiswa = DAO.findAll();
        } else {
            //Mengambil data dari database menggunakan GenericDAO
            //berdasarkan kata kunci yang diketik
            daftarMahasiswa = cariMahasiswa(key);
        }
        // 2. Membersihkan panel target utama sebelum memuat data baru
        panelTarget.removeAll();

        // Mengubah layout panel target menjadi BorderLayout
        panelTarget.setLayout(new BorderLayout());
        // Mengatur warna background utama menjadi biru
        panelTarget.setBackground(new Color(0,204,204));

        // Membuat panel grid khusus untuk menampung kotak/card
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setOpaque(false); // Transparan agar warna biru panelTarget terlihat
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Memberi jarak dari tepi layar

        // 3. Iterasi data dan menambahkannya ke panel grid
        try {
            for (Mahasiswa m : daftarMahasiswa) {
                // Membuat panel 'Card' (box orange) untuk 1 mahasiswa
                // Layout 4 baris 1 kolom agar kolom berisi Nama, NIM, Kelas, panel control 
                JPanel cardPanel = new JPanel(new GridLayout(4, 1, 0, 0));
                cardPanel.setBackground(new Color(255,255,255)); // Warna background putih

                // Memberikan garis tepi tipis membulat (rounded) dan padding/jarak ke dalam
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.MAGENTA, 1, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));

                // Membuat Label Nama & Set warna teks jadi Hitam
                JLabel lblNama = new JLabel("Nama: " + m.getNamaLengkap());
                lblNama.setForeground(Color.BLACK);

                // Membuat Label NIM & Set warna teks jadi Hitam
                String nimAsli = Util.EncryptionUtils.decrypt(m.getNimMahasiswa());
                JLabel lblNIM = new JLabel("NIM: " + nimAsli);
                lblNIM.setForeground(Color.BLACK);

                // Membuat Label Kelas & Set warna teks jadi Hitam
                JLabel lblKelas = new JLabel("Kelas: " + m.getKelas());
                lblKelas.setForeground(Color.BLACK);

                // Membuat panel kontrol 1 baris 2 kolom, berisi tombol edit dan hapus
                JPanel controlPanel = new JPanel(new GridLayout(1, 2, 20, 15));
                controlPanel.setBackground(new Color(237, 125, 49));

                JButton tombolEdit = new JButton("Edit");
                tombolEdit.setBackground(Color.ORANGE);
                tombolEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolEdit.addActionListener((ActionEvent e) -> {
                    // Penyesuaian nama elemen UI berdasarkan snippet yang kamu berikan
                    MahasiswaPanel.txtUID.setText(m.getUidRfid());
                    MahasiswaPanel.txtNIM.setText(m.getNimMahasiswa());
                    MahasiswaPanel.txtNIM.setEnabled(false); 
                    MahasiswaPanel.txtNamaLengkap.setText(m.getNamaLengkap());
                    MahasiswaPanel.txtKelas.setSelectedItem(m.getKelas());
                    MahasiswaPanel.btnUpdate.setEnabled(true);
                    MahasiswaPanel.btnSave.setEnabled(false); 
                });
                
                JButton tombolDelete = new JButton("Delete");
                tombolDelete.setBackground(Color.RED);
                tombolDelete.setForeground(Color.BLACK);
                tombolDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolDelete.addActionListener((ActionEvent e) -> {
                    Object[] options = {"Ya, Hapus", "Batal"};
                    int choice = JOptionPane.showOptionDialog(
                            null, // Parent component
                            "Apakah Anda yakin ingin menghapus data "+m.getNamaLengkap()+"?", // Message (Diperbaiki kalimatnya agar lebih logis)
                            "Konfirmasi Pengelolaan", // Title
                            JOptionPane.YES_NO_OPTION, // Option type
                            JOptionPane.QUESTION_MESSAGE, // Message type
                            null, // Custom icon (null uses default)
                            options, // The array of custom button text
                            options[0] // Default button focused
                    );

                    switch (choice) {
                        case JOptionPane.YES_OPTION -> hapusMahasiswa(m.getNimMahasiswa());
                        case JOptionPane.NO_OPTION -> System.out.println("User memilih: Batal");
                        default -> {
                        }
                    }
                });

                controlPanel.add(tombolEdit);
                controlPanel.add(tombolDelete);

                // Memasukkan label ke dalam cardPanel (box orange)
                cardPanel.add(lblNama);
                cardPanel.add(lblNIM);
                cardPanel.add(lblKelas);
                cardPanel.add(controlPanel);

                // Memasukkan cardPanel utuh ke dalam gridPanel
                gridPanel.add(cardPanel);
            }

            // Memasukkan gridPanel ke bagian ATAS (NORTH) dari panel target.
            panelTarget.add(gridPanel, BorderLayout.NORTH);

            // 4. Me-refresh panel agar perubahan muncul di GUI
            panelTarget.revalidate();
            panelTarget.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    
     *
     *
     * @param key
     * @return
     */
    public List<Mahasiswa> cariMahasiswa(String key) {
        List<Bson> filters = new ArrayList<>();
        // Get all fields from the Mahasiswa class
        for (Field field : Mahasiswa.class.getDeclaredFields()) {
            // Skip the uidRfid field and non-string fields if necessary
            if (field.getName().equals("uidRfid")) {
                continue;
            }
            filters.add(Filters.regex(field.getName(), key, "i"));
        }
        // Search and return Mahasiswa objects directly
        List<Mahasiswa> results = DAO.findMany(Filters.or(filters));
        return results;
    }

    /**
     * 4.UPDATE: Memperbarui data mahasiswa menggunakan filter Bson [5], [6]
     *
     * @param newM
     */
    public void updateMahasiswa(Mahasiswa newM) {
        // Menggunakan "nimMahasiswa" sebagai identifier di database MongoDB
        Bson filter = Filters.eq("nimMahasiswa", newM.getNimMahasiswa());
        Mahasiswa m = DAO.findOne(filter);
        if (m != null) {
            DAO.update(filter, newM);
            MahasiswaPanel.showData("");
            JOptionPane.showMessageDialog(null, "Data berhasil diperbarui!");
        }
    }

    /**
     * 5.DELETE: Menghapus data mahasiswa dari database [5], [6]
     *
     * @param nimM
     */
    public void hapusMahasiswa(String nimM) {
        Bson filter = Filters.eq("nimMahasiswa", nimM);
        DAO.delete(filter); // Menggunakan deleteOne [6]
        MahasiswaPanel.showData("");
        JOptionPane.showMessageDialog(null, "Data mahasiswa berhasil dihapus.");
    }
    public Mahasiswa findByUid(String hashedUid) {
    Bson filter = com.mongodb.client.model.Filters.eq("uidRfid", hashedUid);
    return DAO.findOne(filter);
}
}