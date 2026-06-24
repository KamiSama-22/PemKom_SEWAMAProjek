/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import DAO.GenericDAO;
import GUI.Panel.MahasiswaPanel;
import Objects.Mahasiswa;
import Util.EncryptionUtils;
import Util.SecurityUtils;
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

    // =========================
    // TAMBAH MAHASISWA
    // =========================
    public void tambahMahasiswa(Mahasiswa mahasiswaBaru) {
        try {
            String uidMentah = mahasiswaBaru.getUidRfid();
            String nimMentah = mahasiswaBaru.getNimMahasiswa();

            String uidHashed = SecurityUtils.getHash(uidMentah, SecurityUtils.SHA_256);
            String nimTerenkripsi = EncryptionUtils.encrypt(nimMentah);

            mahasiswaBaru.setUidRfid(uidHashed);
            mahasiswaBaru.setNimMahasiswa(nimTerenkripsi);

            DAO.save(mahasiswaBaru);

            JOptionPane.showMessageDialog(null, "Data mahasiswa berhasil disimpan!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

    public void tambahMahasiswa(String uidRfid, String nimMahasiswa, String namaLengkap, String kelas) {
        Mahasiswa mahasiswaBaru = new Mahasiswa(uidRfid, nimMahasiswa, namaLengkap, kelas);
        tambahMahasiswa(mahasiswaBaru);
    }

    // =========================
    // TAMPIL DATA DI CONSOLE
    // =========================
    public void tampilkanDaftarMahasiswa() {
        List<Mahasiswa> daftar = DAO.findAll();

        System.out.println("--- Daftar Mahasiswa ---");

        for (Mahasiswa m : daftar) {
            try {
                String nimAsli = EncryptionUtils.decrypt(m.getNimMahasiswa());

                System.out.println("UID Hash : " + m.getUidRfid());
                System.out.println("NIM      : " + nimAsli);
                System.out.println("Nama     : " + m.getNamaLengkap());
                System.out.println("Kelas    : " + m.getKelas());
                System.out.println("------------------------");

            } catch (Exception e) {
                System.out.println("Gagal membaca data mahasiswa.");
                e.printStackTrace();
            }
        }
    }

    // =========================
    // TAMPIL DATA KE PANEL
    // =========================
    public void tampilMahasiswa(JPanel panelTarget, String key) {
        List<Mahasiswa> daftarMahasiswa;

        if (key.isEmpty()) {
            daftarMahasiswa = DAO.findAll();
        } else {
            daftarMahasiswa = cariMahasiswa(key);
        }

        panelTarget.removeAll();
        panelTarget.setLayout(new BorderLayout());
        panelTarget.setBackground(new Color(0, 204, 204));

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            for (Mahasiswa m : daftarMahasiswa) {

                String nimAsli = EncryptionUtils.decrypt(m.getNimMahasiswa());

                JPanel cardPanel = new JPanel(new GridLayout(4, 1, 0, 8));
                cardPanel.setBackground(Color.WHITE);

                // Border putih
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));

                Color textColor = Color.BLACK;

                JLabel lblNama = new JLabel("Nama : " + m.getNamaLengkap());
                lblNama.setForeground(textColor);

                JLabel lblNIM = new JLabel("NIM    : " + nimAsli);
                lblNIM.setForeground(textColor);

                JLabel lblKelas = new JLabel("Kelas : " + m.getKelas());
                lblKelas.setForeground(textColor);

                JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 0));
                controlPanel.setBackground(Color.WHITE);

                // =========================
                // TOMBOL EDIT
                // =========================
                JButton tombolEdit = new JButton("EDIT");
                tombolEdit.setBackground(new Color(84, 110, 122));
                tombolEdit.setForeground(Color.WHITE);
                tombolEdit.setBorderPainted(false);
                tombolEdit.setFocusPainted(false);
                tombolEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));

                tombolEdit.addActionListener((ActionEvent e) -> {
                    MahasiswaPanel.txtUID.setText(m.getUidRfid());
                    MahasiswaPanel.txtUID.setEnabled(false);

                    MahasiswaPanel.txtNIM.setText(nimAsli);
                    MahasiswaPanel.txtNIM.setEnabled(false);

                    MahasiswaPanel.txtNamaLengkap.setText(m.getNamaLengkap());
                    MahasiswaPanel.txtKelas.setSelectedItem(m.getKelas());

                    MahasiswaPanel.btnUpdate.setEnabled(true);
                    MahasiswaPanel.btnSave.setEnabled(false);
                });

                // =========================
                // TOMBOL DELETE
                // =========================
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
                            "Apakah Anda yakin ingin menghapus data " + m.getNamaLengkap() + "?",
                            "Konfirmasi Hapus",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

                    switch (choice) {
                        case JOptionPane.YES_OPTION -> hapusMahasiswa(m.getUidRfid());
                        case JOptionPane.NO_OPTION -> System.out.println("User memilih: Batal");
                        default -> {
                        }
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
            JOptionPane.showMessageDialog(null, "Gagal menampilkan data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =========================
    // CARI MAHASISWA
    // =========================
    public List<Mahasiswa> cariMahasiswa(String key) {
        List<Mahasiswa> semuaData = DAO.findAll();
        List<Mahasiswa> hasil = new ArrayList<>();

        for (Mahasiswa m : semuaData) {
            try {
                String nimAsli = EncryptionUtils.decrypt(m.getNimMahasiswa());

                boolean cocok =
                        m.getNamaLengkap().toLowerCase().contains(key.toLowerCase())
                        || nimAsli.toLowerCase().contains(key.toLowerCase())
                        || m.getKelas().toLowerCase().contains(key.toLowerCase());

                if (cocok) {
                    hasil.add(m);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return hasil;
    }

    // =========================
    // UPDATE MAHASISWA
    // =========================
    public void updateMahasiswa(Mahasiswa newM) {
        try {
            Bson filter = Filters.eq("uidRfid", newM.getUidRfid());

            Mahasiswa dataUpdate = new Mahasiswa();

            dataUpdate.setUidRfid(newM.getUidRfid());
            dataUpdate.setNimMahasiswa(EncryptionUtils.encrypt(newM.getNimMahasiswa()));
            dataUpdate.setNamaLengkap(newM.getNamaLengkap());
            dataUpdate.setKelas(newM.getKelas());

            Mahasiswa m = DAO.findOne(filter);

            if (m != null) {
                DAO.update(filter, dataUpdate);
                MahasiswaPanel.showData("");
                JOptionPane.showMessageDialog(null, "Data berhasil diperbarui!");
            } else {
                JOptionPane.showMessageDialog(null, "Data tidak ditemukan!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal update data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =========================
    // HAPUS MAHASISWA
    // =========================
    public void hapusMahasiswa(String uidRfid) {
        try {
            Bson filter = Filters.eq("uidRfid", uidRfid);

            DAO.delete(filter);

            MahasiswaPanel.showData("");
            JOptionPane.showMessageDialog(null, "Data mahasiswa berhasil dihapus.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =========================
    // CARI BERDASARKAN UID HASH
    // =========================
    public Mahasiswa findByUid(String hashedUid) {
        Bson filter = Filters.eq("uidRfid", hashedUid);
        return DAO.findOne(filter);
    }
    public Mahasiswa findByUid(String hashedUid) {
    Bson filter = com.mongodb.client.model.Filters.eq("uidRfid", hashedUid);
    return DAO.findOne(filter);
}
}
