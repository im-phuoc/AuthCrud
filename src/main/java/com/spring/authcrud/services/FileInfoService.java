package com.spring.authcrud.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileInfoService {
    void init();
    String save(MultipartFile file);
    Resource load(String filename);
    boolean delete(String filename);
}
