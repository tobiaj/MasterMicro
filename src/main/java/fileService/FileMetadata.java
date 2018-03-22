package fileService;

import java.io.File;
import java.io.Serializable;

public class FileMetadata implements Serializable{

    private String nameID;
    private String encryptionkey;
    private File file;
    private String fileName;
    private String UUID;


    public FileMetadata(String nameID) {
        this.nameID = nameID;
    }

    public String getName() {
        return nameID;
    }

    public void setName(String nameID) {
        this.nameID = nameID;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setEncryptionkey(String encryptionkey) {
        this.encryptionkey = encryptionkey;
    }

    public String getEncryptionkey() {
        return encryptionkey;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
