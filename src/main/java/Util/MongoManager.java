package Util;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 *
 * @author user
 */
public class MongoManager {
    private static MongoClient mongoClient;
    private static final String DATABASE_NAME = "SEWAMAproject";
    private static CodecRegistry pojoCodecRegistry;

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            // 1. Inisialisasi Registry sekali saja
            pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );

            // 2. Inisiasi koneksi
            mongoClient = MongoClients.create("mongodb://localhost:27017");
        }
        
        // 3. Pastikan return SELALU menyertakan withCodecRegistry
        return mongoClient.getDatabase(DATABASE_NAME).withCodecRegistry(pojoCodecRegistry);
    }
}