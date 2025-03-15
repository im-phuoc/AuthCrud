package com.spring.authcrud.controllers;

import com.spring.authcrud.payload.response.ApiResponse;
import com.spring.authcrud.services.FileInfoService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileInfoController {
    private final FileInfoService fileInfoService;
    public FileInfoController(FileInfoService fileInfoService) {
        this.fileInfoService = fileInfoService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<List<String>>> uploadFile(@RequestParam("files") MultipartFile[] files) {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file: files) {
            try {
                String fileUrl = fileInfoService.save(file);
                fileUrls.add(fileUrl);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false,"Error to upload file "+ e.getMessage(), null));
            }
        }
        return ResponseEntity.ok().body(new ApiResponse<>(true,"Success",fileUrls));
    }

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        Resource file = fileInfoService.load(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @DeleteMapping("/{fileName:.+}")
    public ResponseEntity<ApiResponse<?>> deleteFile(@PathVariable String fileName) {
        boolean status = fileInfoService.delete(fileName);
        return ResponseEntity.ok().body(new ApiResponse<>(status,"Success",null));
    }
}
