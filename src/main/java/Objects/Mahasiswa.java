/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;

/**
 *
 * @author ADVAN
 */
public class Mahasiswa {

    private String uidRfid;
    private String NimMahasiswa;
    private String namaLengkap;
    private String Kelas;
    
    public Mahasiswa() {
    }

    public Mahasiswa(String uidRfid, String NimMahasiswa, String namaLengkap, String Kelas) {
        this.uidRfid = uidRfid;
        this.NimMahasiswa = NimMahasiswa;
        this.namaLengkap = namaLengkap;
        this.Kelas = Kelas;
    }

    public String getUidRfid() {
        return uidRfid;
    }

    public void setUidRfid(String uidRfid) {
        this.uidRfid = uidRfid;
    }

    public String getNimMahasiswa() {
        return NimMahasiswa;
    }

    public void setNimMahasiswa(String NimMahasiswa) {
        this.NimMahasiswa = NimMahasiswa;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getKelas() {
        return Kelas;
    }

    public void setKelas(String Kelas) {
        this.Kelas = Kelas;
    }

    public void UpdateMahasiswa(Mahasiswa M) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}
