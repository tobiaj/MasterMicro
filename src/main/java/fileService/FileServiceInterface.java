package fileService;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;


public interface FileServiceInterface {

    public String storeFile(String nameID, MultipartFile multipartFile);

    public File retrieveFile(String nameID, String fileName);
}
