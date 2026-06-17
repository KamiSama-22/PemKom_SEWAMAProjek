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

    private final GenericDAO<Mahasiswa> DAO;

    public MahasiswaService() {
        this.DAO = new GenericDAO<>("mahasiswa", Mahasiswa.class);
    }

    public void tambahMahasiswa(Mahasiswa mahasiswaBaru) {
        DAO.save(mahasiswaBaru); 
    }

    public void tambahMahasiswa(String uidRfid, String nimMahasiswa, String namaLengkap, String kelas) {
        Mahasiswa mahasiswaBaru = new Mahasiswa(uidRfid, nimMahasiswa, namaLengkap, kelas);
        DAO.save(mahasiswaBaru); 
    }

    public void tampilkanDaftarMahasiswa() {
        List<Mahasiswa> daftar = DAO.findAll();
        System.out.println("--- Daftar Mahasiswa ---");
        for (Mahasiswa m : daftar) {
            System.out.println(m.toString()); 
        }
    }

    public void tampilMahasiswa(JPanel panelTarget, String key) {
        List<Mahasiswa> daftarMahasiswa;
        if (key.isEmpty()) {
            daftarMahasiswa = DAO.findAll();
        } else {
            daftarMahasiswa = cariMahasiswa(key);
        }
        
        panelTarget.removeAll();
        panelTarget.setLayout(new BorderLayout());
        
        // --- TEMA BIRU (Area Data): Latar Belakang Abu-abu Ekstra Muda (#F8F9FA) ---
        panelTarget.setBackground(new Color(0,204,204));

        // Grid untuk jarak antar kartu
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setOpaque(false); 
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

        try {
            for (Mahasiswa m : daftarMahasiswa) {
                
                JPanel cardPanel = new JPanel(new GridLayout(4, 1, 0, 8));
                cardPanel.setBackground(new Color(255,255,255));

                // Garis tepi kartu abu-abu tipis (#E0E0E0)
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));

                // --- Teks menggunakan Abu-abu Gelap ke Hitam (#212121) ---
                Color textColor = new Color(0,0,0);
                
                JLabel lblNama = new JLabel("Nama : " + m.getNamaLengkap());
                lblNama.setForeground(textColor);

                JLabel lblNIM = new JLabel("NIM    : " + m.getNimMahasiswa());
                lblNIM.setForeground(textColor);

                JLabel lblKelas = new JLabel("Kelas : " + m.getKelas());
                lblKelas.setForeground(textColor);

                // Panel Kontrol
                JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 0));
                controlPanel.setBackground(Color.WHITE);

                // --- TOMBOL EDIT ----
                JButton tombolEdit = new JButton("EDIT");
                tombolEdit.setBackground(new Color(84, 110, 122)); 
                tombolEdit.setForeground(Color.WHITE); 
                tombolEdit.setBorderPainted(false);
                tombolEdit.setFocusPainted(false);
                tombolEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolEdit.addActionListener((ActionEvent e) -> {
                    MahasiswaPanel.txtUID.setText(m.getUidRfid());
                    MahasiswaPanel.txtNIM.setText(m.getNimMahasiswa());
                    MahasiswaPanel.txtNIM.setEnabled(false); 
                    MahasiswaPanel.txtNamaLengkap.setText(m.getNamaLengkap());
                    MahasiswaPanel.txtKelas.setSelectedItem(m.getKelas());
                    MahasiswaPanel.btnUpdate.setEnabled(true);
                    MahasiswaPanel.btnSave.setEnabled(false); 
                });
                
                // --- TOMBOL DELETE: Maroon Gelap (#8B0000) ---
                JButton tombolDelete = new JButton("DELETE");
                tombolDelete.setBackground(new Color(139, 0, 0)); 
                tombolDelete.setForeground(Color.WHITE); 
                tombolDelete.setBorderPainted(false);
                tombolDelete.setFocusPainted(false); 
                tombolDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolDelete.addActionListener((ActionEvent e) -> {
                    Object[] options = {"Ya, Hapus", "Batal"};
                    int choice = JOptionPane.showOptionDialog(
                            null, 
                            "Apakah Anda yakin ingin menghapus data "+m.getNamaLengkap()+"?", 
                            "Konfirmasi Hapus", 
                            JOptionPane.YES_NO_OPTION, 
                            JOptionPane.QUESTION_MESSAGE, 
                            null, 
                            options, 
                            options[0] 
                    );

                    switch (choice) {
                        case JOptionPane.YES_OPTION -> hapusMahasiswa(m.getNimMahasiswa());
                        case JOptionPane.NO_OPTION -> System.out.println("User memilih: Batal");
                        default -> {}
                    }
                });

                controlPanel.add(tombolEdit);
                controlPanel.add(tombolDelete);

                cardPanel.add(lblNama);
                cardPanel.add(lblNIM);
                cardPanel.add(lblKelas);
                cardPanel.add(controlPanel);

                gridPanel.add(cardPanel);
            }

            panelTarget.add(gridPanel, BorderLayout.NORTH);

            panelTarget.revalidate();
            panelTarget.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Mahasiswa> cariMahasiswa(String key) {
        List<Bson> filters = new ArrayList<>();
        for (Field field : Mahasiswa.class.getDeclaredFields()) {
            if (field.getName().equals("uidRfid")) {
                continue;
            }
            filters.add(Filters.regex(field.getName(), key, "i"));
        }
        List<Mahasiswa> results = DAO.findMany(Filters.or(filters));
        return results;
    }

    public void updateMahasiswa(Mahasiswa newM) {
        Bson filter = Filters.eq("nimMahasiswa", newM.getNimMahasiswa());
        Mahasiswa m = DAO.findOne(filter);
        if (m != null) {
            DAO.update(filter, newM);
            MahasiswaPanel.showData("");
            JOptionPane.showMessageDialog(null, "Data berhasil diperbarui!");
        }
    }

    public void hapusMahasiswa(String nimM) {
        Bson filter = Filters.eq("nimMahasiswa", nimM);
        DAO.delete(filter); 
        MahasiswaPanel.showData("");
        JOptionPane.showMessageDialog(null, "Data mahasiswa berhasil dihapus.");
    }
}