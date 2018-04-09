package fileService;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;


public interface FileServiceInterface {

    String storeFile(String nameID, MultipartFile multipartFile);

    List<String> getFiles(String id);

    List<String> getSafeFiles(String id);

    List<String> getNoneSafeFiles(String id);

    File getFile(String id, String name);

    File getSafeFile(String id, String name);

    String storeSafeFile(String nameID, MultipartFile multipartFile);

    String deleteFile(String id, String fileID);
}
