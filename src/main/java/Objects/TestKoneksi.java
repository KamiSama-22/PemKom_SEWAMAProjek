/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class TestKoneksi {
    public static void main(String[] args) {
        try {
            System.out.println("Sedang mencoba menghubungkan ke database...");
            
            // 1. Memanggil koneksi melalui MongoManajer
            // Pastikan nama method di MongoManajer adalah getDatabase()
            MongoDatabase database = MongoManajer.getDatabase();
            
            // 2. Verifikasi koneksi
            Document ping = new Document("ping", 1);
            database.runCommand(ping);
            
            System.out.println("=========================================");
            System.out.println("STATUS: KONEKSI BERHASIL!");
            System.out.println("Terhubung ke Database: " + database.getName());
            System.out.println("=========================================");
            
            // 3. Menampilkan daftar koleksi
            System.out.println("Daftar Koleksi di " + database.getName() + ":");
            for (String name : database.listCollectionNames()) {
                System.out.println("- " + name);
            }

            // 4. PROSES TAMBAH DATA (Harus di dalam blok try agar variabel 'database' terbaca)
            System.out.println("\nMenambahkan data baru...");
            MongoCollection<Document> collection = database.getCollection("Absensi_log");

            Document dataBaru = new Document("nim", "220406") 
                    .append("nama", "Muhammad Irhash Syahid")
                    .append("waktu_tap", java.time.LocalDateTime.now().toString())
                    .append("status", "Hadir");

            collection.insertOne(dataBaru);

            System.out.println("-----------------------------------------");
            System.out.println("DATA BERHASIL DITAMBAHKAN!");
            System.out.println("-----------------------------------------");

        } catch (Exception e) {
            System.err.println("=========================================");
            System.err.println("STATUS: TERJADI MASALAH!");
            System.err.println("Pesan Error: " + e.getMessage());
            System.err.println("=========================================");
        }
    }
}