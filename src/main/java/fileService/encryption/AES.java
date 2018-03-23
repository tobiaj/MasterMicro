package fileService.encryption;

import fileService.FileMetadata;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Base64;


public class AES {
    private static final String ALGORITHM = "AES";
    private static final String AES_MODE = "AES/CBC/PKCS5Padding";
    private static final String KEY_MODE = "PBKDF2WithHmacSHA1";
    private static final int SALT_IV_SIZE = 16;
    private static final int KEY_SIZE = 128;
    private static File folder = new File("C:\\Users\\tobias.johansson\\Documents\\TobiasThesis\\outputFilesFromEncryption");


    public void processEncrypt(FileMetadata fileMetadata) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidParameterSpecException, BadPaddingException, IllegalBlockSizeException{

        File outputFile = new File(folder + "\\"+"encrypted-" + fileMetadata.getFile().getName());
        File inputFile = fileMetadata.getFile();
        String key = encrypt(inputFile, outputFile);
        fileMetadata.setEncryptionkey(key);



    }

    public void processDecrypt(FileMetadata fileMetadata) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidParameterSpecException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

        File outputFile = new File(folder+ "\\" + "decrypted-" + fileMetadata.getFile().getName());
        File inputFile = fileMetadata.getFile();
        String key = fileMetadata.getEncryptionkey();
        File output = decrypt(key, inputFile, outputFile);
        fileMetadata.setFile(output);

    }


    public String encrypt(File inputFile, File outputFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidParameterSpecException, BadPaddingException, IllegalBlockSizeException {

        FileInputStream inFile = new FileInputStream(inputFile);

        FileOutputStream outFile = new FileOutputStream(outputFile);

        byte[] salt = createSalt();
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        outFile.write(salt);

        SecretKey secret = createSecretKey(salt);


        byte[] store = secret.getEncoded();
        String key = Base64.getEncoder().withoutPadding().encodeToString(store);

        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();


        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        outFile.write(iv);

        int inputFileSize = (int) inputFile.length();
        byte[] input = new byte[inputFileSize];
        int bytesRead;

        while ((bytesRead = inFile.read(input)) != -1)

        {
            byte[] output = cipher.update(input, 0, bytesRead);
            if (output != null)
                outFile.write(output);
        }

        byte[] output = cipher.doFinal();
        if (output != null)
            outFile.write(output);

        inFile.close();
        outFile.flush();
        outFile.close();

        System.out.println("File Encrypted.");

        return key;
    }



    private File decrypt(String key, File inputFile, File outputFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidParameterSpecException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        {
            FileInputStream input = new FileInputStream(inputFile);

            byte[] salt = new byte[16];
            byte[] iv = new byte[16];
            input.read(salt);
            input.read(iv);

            byte[] encoded = Base64.getDecoder().decode(key);
            SecretKey aesKey = new SecretKeySpec(encoded, "AES");

            Cipher cipher = Cipher.getInstance(AES_MODE);
            cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(iv));

            FileOutputStream out = new FileOutputStream(outputFile);


            int inputFileSize = (int) inputFile.length();
            byte[] in = new byte[inputFileSize];
            int read;
            while ((read = input.read(in)) != -1) {
                byte[] output = cipher.update(in, 0, read);
                if (output != null)
                    out.write(output);
            }

            byte[] output = cipher.doFinal();
            if (output != null)
                out.write(output);
            input.close();
            out.flush();
            out.close();
            System.out.println("File Decrypted.");
            return outputFile;
        }
    }

    private SecretKey createSecretKey(byte[] salt) throws NoSuchAlgorithmException {

        String password = "password";
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(KEY_MODE);
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65556, KEY_SIZE);

        SecretKey secretKey = null;
        try {
            secretKey = secretKeyFactory.generateSecret(keySpec);
            SecretKey newSecretKey = new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);
            return newSecretKey;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }


        return null;
    }

    private byte[] createSalt() {

        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_IV_SIZE];
        secureRandom.nextBytes(salt);
        return salt;

    }

}
