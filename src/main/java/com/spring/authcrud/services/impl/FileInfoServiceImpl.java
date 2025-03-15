package com.spring.authcrud.services.impl;

import com.spring.authcrud.models.FileInfo;
import com.spring.authcrud.models.User;
import com.spring.authcrud.payload.response.UserResponse;
import com.spring.authcrud.repository.FileInfoRepository;
import com.spring.authcrud.repository.UserRepository;
import com.spring.authcrud.services.FileInfoService;
import com.spring.authcrud.services.UserService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileInfoServiceImpl implements FileInfoService {
    private final static Path root = Paths.get("uploads");

    private final FileInfoRepository fileInfoRepository;

    private final UserService userService;

    private final UserRepository userRepository;


    public FileInfoServiceImpl(FileInfoRepository fileInfoRepository, UserService userService, UserRepository userRepository) {
        this.fileInfoRepository = fileInfoRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Error creating directory: " + root, e);
        }
    }

    @Override
    public String save(MultipartFile file) {
        UserResponse getUserInfo = userService.getInfo();
        User user = userRepository.findByUsername(getUserInfo.getUsername()).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            if (originalFileName.isBlank()) {
                throw new RuntimeException("Filename is empty");
            }

            if (!isValidFileType(file)) {
                throw new RuntimeException("Invalid file type. Only JPG and PNG are allowed.");
            }

            if (!Files.exists(root)) {
                init();
            }

            if (originalFileName.contains("..")) {
                throw new RuntimeException("Invalid file path: " + originalFileName);
            }

            String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

            Path target = root.resolve(uniqueFileName);

            Optional<FileInfo> existingFile = fileInfoRepository.findByFileName(originalFileName);
            if (existingFile.isPresent()) {
                throw new RuntimeException("File already exists in database: " + originalFileName);
            }

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            String fileUrl = "/uploads/" + uniqueFileName;
            FileInfo fileInfo = new FileInfo(uniqueFileName, fileUrl,user);
            fileInfoRepository.save(fileInfo);

            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException("Error saving file: " + e.getMessage(), e);
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + filename, e);
        }
    }

    @PreAuthorize("@fileInfoServiceImpl.getOwnerUsername(#filename) == authentication.name")
    @Override
    public boolean delete(String filename) {
        try {
            Path file = root.resolve(filename);
            boolean deleted = Files.deleteIfExists(file);
            fileInfoRepository.findByFileName(filename).ifPresent(fileInfoRepository::delete);
            return deleted;
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file: " + filename, e);
        }
    }

    private boolean isValidFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }

        return contentType.equals("image/jpeg") || contentType.equals("image/png");
    }

    public String getOwnerUsername(String filename) {
        FileInfo fileInfo = fileInfoRepository.findByFileName(filename)
                .orElseThrow(() -> new RuntimeException("File not found: " + filename));
        return fileInfo.getUser().getUsername();
    }
}
