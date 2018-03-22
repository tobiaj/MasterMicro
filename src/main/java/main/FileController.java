package main;

import fileService.FileService;
import fileService.FileServiceInterface;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
@RestController
public class FileController {

    FileServiceInterface fileService;
    public FileController(){
        this.fileService = new FileService();
    }


    @RequestMapping("/")
    public String home(){
        return "Hello";
    }

    @RequestMapping(value = "/storeFile", method = RequestMethod.POST)
    public ResponseEntity<String> storeFile(@RequestParam("nameID") String nameID, @RequestParam(value = "file", required = false) MultipartFile multipartFile) throws Exception {


        String returnMessage = fileService.storeFile(nameID, multipartFile);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/json"));

        return new ResponseEntity<String>(returnMessage, HttpStatus.OK);


    }


    @GetMapping(value = "/retrieveName/{nameID}/fileID/{fileID}", produces = "application/pdf")
    public ResponseEntity<byte[]> retrieveFile(@PathVariable("nameID") String nameID, @PathVariable("fileID") String fileID) throws Exception {

        File file = fileService.retrieveFile(nameID, fileID);
        byte[] document = FileCopyUtils.copyToByteArray(file);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "pdf"));
        header.set("Content-Disposition", "inline; filename=" + file.getName());
        header.setContentLength(document.length);

        return new ResponseEntity<byte[]>(document, header, HttpStatus.OK);


    }

    @GetMapping(value = "/test/{id}")
    public String testing(@PathVariable("id") String id){

        return "Woho" + id;
    }





    void handleException(Exception e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(OriginalExceptionFromAnotherApi.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handle(OriginalExceptionFromAnotherApi e) {

        return new ErrorResponse(e.getMessage()); // use message from the original exception

    }

    /**
     * Defines the JSON output format of error responses
     */
    private static class ErrorResponse {
        public String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }


    private static class OriginalExceptionFromAnotherApi extends RuntimeException {
        public OriginalExceptionFromAnotherApi(String message) {
            super(message);
        }
    }
}