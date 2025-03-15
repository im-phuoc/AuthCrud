package com.spring.authcrud.repository;

import com.spring.authcrud.models.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    Optional<FileInfo> findByFileName(String fileName);
}
