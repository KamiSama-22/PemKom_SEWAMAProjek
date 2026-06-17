/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;

import org.bson.codecs.pojo.annotations.BsonProperty;

/**
 *
 * @author ADVAN
 */
public class Mahasiswa {

    // Mengunci nama properti agar dipetakan dengan benar oleh Codec MongoDB
    @BsonProperty(value = "uidRfid")
    private String uidRfid;
    
    @BsonProperty(value = "nimMahasiswa")
    private String nimMahasiswa; // Diubah menjadi huruf kecil di awal (nimMahasiswa)
    
    @BsonProperty(value = "namaLengkap")
    private String namaLengkap;
    
    @BsonProperty(value = "kelas")
    private String kelas;        // Diubah menjadi huruf kecil di awal (kelas)
    
    // Constructor Kosong (Wajib untuk MongoDB POJO Codec)
    public Mahasiswa() {
    }

    public Mahasiswa(String uidRfid, String nimMahasiswa, String namaLengkap, String kelas) {
        this.uidRfid = uidRfid;
        this.nimMahasiswa = nimMahasiswa;
        this.namaLengkap = namaLengkap;
        this.kelas = kelas;
    }

    public String getUidRfid() {
        return uidRfid;
    }

    public void setUidRfid(String uidRfid) {
        this.uidRfid = uidRfid;
    }

    public String getNimMahasiswa() {
        return nimMahasiswa;
    }

    public void setNimMahasiswa(String nimMahasiswa) {
        this.nimMahasiswa = nimMahasiswa;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }
}