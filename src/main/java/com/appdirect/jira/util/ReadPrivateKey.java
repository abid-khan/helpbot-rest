package com.appdirect.jira.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import sun.misc.BASE64Decoder;

/**
 * Created by abidkhan on 07/05/17.
 */
public class ReadPrivateKey {

    public  static void main(String[] args) throws Exception {
        System.out.println(readPrivateKey1("/Users/abidkhan/.ssh/jira.pem"));
    }

    public static String readPrivateKey(String  file) throws Exception {
        // read key bytes
        FileInputStream in = new FileInputStream(new File(file));
        byte[] keyBytes = new byte[in.available()];
        in.read(keyBytes);
        in.close();

        String privateKey = new String(keyBytes, "UTF-8");
        privateKey = privateKey.replaceAll("(-+BEGIN RSA PRIVATE KEY-+\\r?\\n|-+END RSA PRIVATE KEY-+\\r?\\n?)", "");

        // don't use this for real projects!
        BASE64Decoder decoder = new BASE64Decoder();
        keyBytes = decoder.decodeBuffer(privateKey);

        // generate public key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey publicKey = keyFactory.generatePrivate(spec);

        return privateKey;
    }

    public static String readPrivateKey1(String  file) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        PrivateKey privateKey=null;
        PemReader pemReader = new PemReader(new InputStreamReader(new FileInputStream(file)));
        try {
            PemObject pemObject = pemReader.readPemObject();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(pemObject.getContent());
            privateKey =  factory.generatePrivate(privKeySpec);
            System.out.println("privateKey = "+privateKey);
        } finally {
            pemReader.close();
        }

        return  privateKey.getAlgorithm();

    }

}
