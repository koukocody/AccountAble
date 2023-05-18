/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.plaid;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author cody6
 */
public class InitializeFirestore {
    
    public static Firestore db;
    
    public static void initialize() throws FileNotFoundException, IOException{
        File currentDir = new File("");
        InputStream serviceAccount = new FileInputStream(currentDir.getAbsolutePath() + "\\src\\main\\java\\com\\plaid\\accountable-FB-key.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(credentials)
            .build();
        FirebaseApp.initializeApp(options);

        db = FirestoreClient.getFirestore();
    }
}
