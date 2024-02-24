package main;

import java.io.*;
import java.security.*;

// This program generates public-private key pairs and stores them in files
public class Gen {
    public static void main(String[] args) {
        try {
            // Generate Alice's key pair
            KeyPairGenerator aliceKeyGen = KeyPairGenerator.getInstance("RSA");
            KeyPair aliceKeyPair = aliceKeyGen.generateKeyPair();
            
            // Save Alice's keys to files
            ObjectOutputStream alicePubOut = new ObjectOutputStream(new FileOutputStream("alice_pub.key"));
            alicePubOut.writeObject(aliceKeyPair.getPublic());
            alicePubOut.close();
            
            ObjectOutputStream alicePrivOut = new ObjectOutputStream(new FileOutputStream("alice_priv.key"));
            alicePrivOut.writeObject(aliceKeyPair.getPrivate());
            alicePrivOut.close();
            
            // Generate Bob's key pair
            KeyPairGenerator bobKeyGen = KeyPairGenerator.getInstance("RSA");
            KeyPair bobKeyPair = bobKeyGen.generateKeyPair();
            
            // Save Bob's keys to files
            ObjectOutputStream bobPubOut = new ObjectOutputStream(new FileOutputStream("bob_pub.key"));
            bobPubOut.writeObject(bobKeyPair.getPublic());
            bobPubOut.close();
            
            ObjectOutputStream bobPrivOut = new ObjectOutputStream(new FileOutputStream("bob_priv.key"));
            bobPrivOut.writeObject(bobKeyPair.getPrivate());
            bobPrivOut.close();
            
            System.out.println("Key pairs generated and saved successfully.");
            
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }
}

