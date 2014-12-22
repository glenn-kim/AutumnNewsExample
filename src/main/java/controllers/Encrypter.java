package controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by infinitu on 14. 12. 18..
 */
public class Encrypter {
    private static Encrypter instance;
    private final MessageDigest md;
    private final static byte[] SALT = "Honux's DataBase Basic".getBytes();

    public static Encrypter getInstance(){
        if(instance==null)
            try {
                instance = new Encrypter();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        return instance;
    }

    private Encrypter() throws NoSuchAlgorithmException {
        this.md = MessageDigest.getInstance("SHA-256");
    }

    private void reset(){
        md.reset();
        md.update(SALT);
    }

    public String password(String pass){
        md.reset();
        return new String(md.digest(pass.getBytes()));
    }
}
