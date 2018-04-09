package main;

import fileService.FileService;
import fileService.FileServiceInterface;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
@EnableResourceServer
@EnableWebSecurity
@RestController
public class FileController extends WebSecurityConfigurerAdapter {


    FileServiceInterface fileService;

    public FileController(){
        this.fileService = new FileService();
    }


    @RequestMapping("/")
    public String home(){
        return "Hello";
    }

    @RequestMapping(value = "/storage/user/{id}/file", method = RequestMethod.POST)
    public ResponseEntity<String> storeFile(@PathVariable("id") String id, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {


        String returnMessage = fileService.storeFile(id, multipartFile);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/json"));

        return new ResponseEntity<String>(returnMessage, HttpStatus.OK);


    }

    @RequestMapping(value = "/storage/user/{id}/safeFile", method = RequestMethod.POST)
    public ResponseEntity<String> storeSafeFile(@PathVariable("id") String id, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {


        String returnMessage = fileService.storeSafeFile(id, multipartFile);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/json"));

        return new ResponseEntity<String>(returnMessage, HttpStatus.OK);
    }

    @RequestMapping(value = "/storage/user/{id}/deleteFile/{fileID}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteFile(@PathVariable("id") String id, @PathVariable("fileID") String fileID) {

        String returnMessage = fileService.deleteFile(id, fileID);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/json"));

        return new ResponseEntity<String>(returnMessage, HttpStatus.OK);


    }

    @RequestMapping(value = "/storage/user/{id}/file/{fileID}", produces = "application/pdf", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getFile(@PathVariable("id") String id, @PathVariable("fileID") String fileID) throws Exception {

        File file = fileService.getFile(id, fileID);
        byte[] document = FileCopyUtils.copyToByteArray(file);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "pdf"));
        header.set("Content-Disposition", "inline; filename=" + file.getName());
        header.setContentLength(document.length);

        return new ResponseEntity<byte[]>(document, header, HttpStatus.OK);

    }

    @RequestMapping(value = "/storage/user/{id}/safeFile/{fileID}", produces = "application/pdf", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getSafeFile(@PathVariable("id") String id, @PathVariable("fileID") String fileID) throws Exception {

        File file = fileService.getSafeFile(id, fileID);
        byte[] document = FileCopyUtils.copyToByteArray(file);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "pdf"));
        header.set("Content-Disposition", "inline; filename=" + file.getName());
        header.setContentLength(document.length);

        return new ResponseEntity<byte[]>(document, header, HttpStatus.OK);

    }

    @RequestMapping(value = "/user/{id}/files", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<List<String>> getAllFiles(@PathVariable("id") String id) {

        List<String> result = fileService.getFiles(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/json"));

        return new ResponseEntity<List<String>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}/safeFiles", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<List<String>> getSafeFiles(@PathVariable("id") String id) {

        List<String> result = fileService.getSafeFiles(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/json"));

        return new ResponseEntity<List<String>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}/noneSafeFiles", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<List<String>> getNoneSafeFiles(@PathVariable("id") String id) {

        List<String> result = fileService.getNoneSafeFiles(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/json"));

        return new ResponseEntity<List<String>>(result, HttpStatus.OK);
    }


    @Bean
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setClientId("admin");
        tokenServices.setClientSecret("password");
        tokenServices.setCheckTokenEndpointUrl("http://localhost:8888/oauth/check_token");
        return tokenServices;
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
        authenticationManager.setTokenServices(tokenServices());
        return authenticationManager;
    }

}