package com.vizexplorer.dataadapter.admin.rest;

import com.vizexplorer.dataadapter.admin.services.FileService;
import com.vizexplorer.dataadapter.admin.services.dto.FileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(value = FileServerController.URI_PREFIX, produces="application/json")
public class FileServerController {

    private static final Logger LOG = LoggerFactory.getLogger(FileServerController.class);

    public static final String URI_PREFIX = "/services/files";
    public static final String LIST_PREFIX = "/list/";
    public static final String DOWNLOAD_PREFIX = "/download/";
    public static final String UPLOAD_PREFIX = "/upload/";
    public static final String DELETE_PREFIX = "/delete/";
    public static final String CREATEDIR_PREFIX = "/createdir/";

    @Autowired
    private FileService fileService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping(value=DOWNLOAD_PREFIX+"{filePath}" )
    public ResponseEntity<Resource> downloadFile(@PathVariable(value="filePath") String file) {
        try {
            Path filePath = Paths.get(file);
            LOG.info("downloadFile: {}", filePath);
            Resource resource = fileService.loadFileAsResource(filePath);
            String contentType = "application/octet-stream";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value=LIST_PREFIX)
    public ResponseEntity<FileList> getFiles() {
        return getFiles("/");
    }
    @GetMapping(value={LIST_PREFIX + "{filePath}"})
    public ResponseEntity<FileList> getFiles(@PathVariable(value="filePath") String path) {
        try {
            Path filePath = Paths.get(path);
            LOG.info("getFiles: {}", filePath);
            FileList fileInfo = fileService.getFilesInfo(filePath);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(fileInfo);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Issues: OpenAPI 3.1 does not support wildcard paths, so slashes in the request won't map properly until they
     * implement RFC-6570 (https://www.rfc-editor.org/rfc/rfc6570)
     * Using a PathVariable so swagger3 generates the correct doc. Otherwise (if using **) you won't be able to use
     * the swagger-ui (but you would be allowed slashes in the file path)
     *
     * For now, no slashes. (should be ({/filePath*} to replace {filePath})
     * Discussion here: https://groups.google.com/g/swagger-swaggersocket/c/11EoThjJmMM
     */
    @PostMapping(value=UPLOAD_PREFIX + "{filePath}")
    public ResponseEntity<Resource> fileUpload(@RequestParam("file") MultipartFile file,
                                               @PathVariable("filePath") String fp) {
        try {
            Path filePath = Paths.get(fp);
            LOG.info("upload: {}", filePath);
            fileService.saveFile(filePath, file.getInputStream());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(DELETE_PREFIX + "{filePath}")
    public ResponseEntity<Resource> delete(@PathVariable(value="filePath") String file) {
        try {
            Path filePath = Paths.get(file);
            LOG.info("delete: {}", filePath);
            fileService.delete(filePath);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(CREATEDIR_PREFIX + "{filePath}")
    public ResponseEntity<Resource> createDirectory(@PathVariable(value="filePath") String file) {
        try {
            Path filePath = Paths.get(file);
            LOG.info("createDirectory: {}", filePath);
            fileService.createDirectory(filePath);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
