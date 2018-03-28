package fileService;


import fileService.database.DatabaseHandler;
import fileService.encryption.AES;
import org.springframework.web.multipart.MultipartFile;
import storage.MinioStorage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

public class FileService implements FileServiceInterface {

    private MinioStorage minioStorage;
    private AES aes;
    private DatabaseHandler databaseHandler;

    public FileService(){
        this.minioStorage = new MinioStorage();
        this.aes = new AES();
        this.databaseHandler = new DatabaseHandler();
    }


    public String storeFile(String name, MultipartFile multipartFile) {

        File file = extractFile(multipartFile);

        FileMetadata fileMetadata = new FileMetadata(name);
        fileMetadata.setFile(file);

        try {

            aes.processEncrypt(fileMetadata);
            System.out.println("Done encryption");

            if (minioStorage.uploadToStorage(fileMetadata)){

                databaseHandler.storeKeys(fileMetadata);

                return "Done upload";
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        }

        return "Failed";

    }


    public File retrieveFile(String nameID, String fileID){

        String result = databaseHandler.retrieveEncryptionkey(nameID, fileID);
        String[] split = result.split(",");

        FileMetadata fileMetadata = new FileMetadata(nameID);
        fileMetadata.setEncryptionkey(split[0]);
        fileMetadata.setFileName(split[1]);
        fileMetadata.setUUID(split[2]);

        if (minioStorage.retrieveFromStorage(fileMetadata)){

            try {
                aes.processDecrypt(fileMetadata);
                return fileMetadata.getFile();

            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (InvalidParameterSpecException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private File extractFile(MultipartFile file)
    {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convFile;
    }
}
