package storage;

import fileService.FileMetadata;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class MinioStorage {

    MinioClient minioClient;
    private static File folder = new File("C:\\Users\\tobias.johansson\\Documents\\TobiasThesis\\outputFilesFromEncryption");

    public MinioStorage() {
        try {
            this.minioClient = new MinioClient("http://127.0.0.1:9000", "PZ1KE1344KA040RSV62N","uAUp9c3W30zf+vH2JeOcuNC+rjfmLRg73Rew05TA");
        } catch (InvalidEndpointException e) {
            e.printStackTrace();
        } catch (InvalidPortException e) {
            e.printStackTrace();
        }

    }


    public boolean uploadToStorage(FileMetadata fileMetadata){

       String bucketName = "proceedo";

        String id = String.valueOf(UUID.randomUUID());
        fileMetadata.setUUID(id);

        try {
            boolean bucketExist = minioClient.bucketExists(bucketName);
            if (bucketExist){
                //System.out.println("Bucket exists");
            }
            else{
                minioClient.makeBucket(bucketName);
            }

            minioClient.putObject(bucketName, id+"-"+fileMetadata.getFile().getName(), folder + "\\encrypted-" + fileMetadata.getFile().getName());
            //System.out.println("Upload successful");
            return true;


        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoResponseException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InvalidBucketNameException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (RegionConflictException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean retrieveFromStorage(FileMetadata fileMetadata){


        String bucketName = "proceedo";
        String fetchedName = folder + "\\" + fileMetadata.getUUID()+"-"+fileMetadata.getFileName();
        try {

            boolean bucketExist = minioClient.bucketExists(bucketName);
            if (bucketExist){
                System.out.println("Bucket exists");
            }
            else{
                System.out.println("Bucket does not exist, or wrong bucket name");
            }

            minioClient.getObject(bucketName, fileMetadata.getUUID()+"-"+fileMetadata.getFileName(), fetchedName);
            File file = new File(fetchedName);
            fileMetadata.setFile(file);

            System.out.println("download successful");
            return true;

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (InvalidBucketNameException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoResponseException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }

        return false;

    }

}
