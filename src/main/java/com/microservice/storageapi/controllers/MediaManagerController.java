package com.microservice.storageapi.controllers;

import com.microservice.storageapi.models.FileInfo;
import com.microservice.storageapi.payloads.response.MessageResponse;
import com.microservice.storageapi.services.MediaManagerStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mm")
public class MediaManagerController {

    private static final Logger logger = LoggerFactory.getLogger(MediaManagerController.class);

    @Autowired
    MediaManagerStorageService mediaManagerStorageService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ROLE_STORAGE_ADMIN')")
    public ResponseEntity<MessageResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            mediaManagerStorageService.save(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.ok().body(new MessageResponse(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
        }
    }

    @GetMapping("/uploadfolder")
    @PreAuthorize("hasRole('ROLE_STORAGE_ADMIN')")
    public ResponseEntity<MessageResponse> uploadFolder(@RequestParam("folderpath") String folderPath) {
        String message = "";
        try {
            mediaManagerStorageService.saveFolder(folderPath);
            message = "Uploaded the folder successfully: " + folderPath;
            return ResponseEntity.ok().body(new MessageResponse(message));
        } catch (Exception e) {
            message = "Could not upload the folder: " + folderPath + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
        }
    }

    @GetMapping("/files")
    @PreAuthorize("hasRole('ROLE_STORAGE_ADMIN')")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = mediaManagerStorageService.loadAll("media-manager").map(path -> {
            String filename = path.getFileName().toString();
            String url = organizePath(path.toString());
            String str_path = ".";
            if(path.getParent() != null) {
                str_path = organizePath(path.getParent().toString());
            }
            return new FileInfo(filename, url, str_path);
        }).collect(Collectors.toList());

        return ResponseEntity.ok().body(fileInfos);
    }

    @GetMapping("/files/{foldername:.+}")
    @PreAuthorize("hasRole('ROLE_STORAGE_ADMIN')")
    public ResponseEntity<List<FileInfo>> getListFiles(@PathVariable String foldername) {
        List<FileInfo> fileInfos = mediaManagerStorageService.loadAll(foldername).map(path -> {
            String filename = path.getFileName().toString();
            String url = organizePath(path.toString());
            String str_path = ".";
            if(path.getParent() != null) {
                str_path = organizePath(path.getParent().toString());
            }
            return new FileInfo(filename, url, str_path);
        }).collect(Collectors.toList());

        return ResponseEntity.ok().body(fileInfos);
    }

    @GetMapping("/file/**")
    @PreAuthorize("hasRole('ROLE_STORAGE_ADMIN')")
    public ResponseEntity<Resource> getFile(HttpServletRequest request) {
        String filename = extractPath(request);
        logger.info("About to find file {}", filename);
        Resource file = mediaManagerStorageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    private String extractPath(HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String matchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(matchPattern, path);
    }

    private String organizePath(String originalPath) {
        String organizedPath = originalPath;
        String osName = System.getProperty("os.name");
        if(osName.matches("Windows.*")) {
            String[] folders = originalPath.split("\\\\");
            organizedPath = folders[0];
            for(int i = 1; i < folders.length; i++) {
                organizedPath = organizedPath + "/" + folders[i];
            }
        }
        return organizedPath;
    }

}
