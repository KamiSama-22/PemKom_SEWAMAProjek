/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sewama_project;

/**
 *
 * @author ADVAN
 */
public class Mahasiswa {

    private String uidRfid;
    private String idMahasiswa;
    private String namaLengkap;
    private String Kelas;
    
    public Mahasiswa() {
    }

    public Mahasiswa(String uidRfid, String idMahasiswa, String namaLengkap, String Kelas) {
        this.uidRfid = uidRfid;
        this.idMahasiswa = idMahasiswa;
        this.namaLengkap = namaLengkap;
        this.Kelas = Kelas;
    }

    public String getUidRfid() {
        return uidRfid;
    }

    public void setUidRfid(String uidRfid) {
        this.uidRfid = uidRfid;
    }

    public String getIdMahasiswa() {
        return idMahasiswa;
    }

    public void setIdMahasiswa(String idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
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
    
    
}
