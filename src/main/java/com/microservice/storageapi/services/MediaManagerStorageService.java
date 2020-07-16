package com.microservice.storageapi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class MediaManagerStorageService {

    private static final Logger logger = LoggerFactory.getLogger(MediaManagerStorageService.class);

    private final Path root = Paths.get("media-manager");

    public void init() {
        try {
            if(Files.notExists(root)){
                Files.createDirectory(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload.");
        }
    }

    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public void saveFolder(String folderPath) {
        Path path = Paths.get(folderPath);
        try {
            if(Files.notExists(path)){
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize new folder.");
        }
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public Stream<Path> loadAll(String folderPath) {
        try {
            Path folder = createPath(folderPath);
            if(Files.notExists(folder)){
                createFolder(folder);
            }
            return Files.walk(folder).filter(file -> !Files.isDirectory(file)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public void createFolder(Path path) {
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not create the directory.");
        }
    }

    public Path createPath(String folderPath) {
        boolean isFullpath = false;
        String[] folderNames = folderPath.split("/");
        for (String folderName : folderNames) {
            if (folderName.equals("media-manager")) {
                isFullpath = true;
                break;
            }
        }
        if(isFullpath) {
            return Paths.get(folderPath);
        }else{
            return Paths.get("media-manager/" + folderPath);
        }
    }

}
