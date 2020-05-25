package com.microservice.storageapi.controllers;

import com.microservice.storageapi.models.FileInfo;
import com.microservice.storageapi.payloads.response.MessageResponse;
import com.microservice.storageapi.services.MediaManagerStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mm")
public class MediaManagerController {

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

    @GetMapping("/files")
    @PreAuthorize("hasRole('ROLE_STORAGE_ADMIN')")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = mediaManagerStorageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(MediaManagerController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.ok().body(fileInfos);
    }

    @GetMapping("/file/{filename:.+}")
    @PreAuthorize("hasRole('ROLE_STORAGE_ADMIN')")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = mediaManagerStorageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
