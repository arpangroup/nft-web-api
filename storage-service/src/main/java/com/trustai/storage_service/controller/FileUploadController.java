package com.trustai.storage_service.controller;

import com.trustai.storage_service.service.StorageService;
import com.trustai.storage_service.util.StorageUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
public class FileUploadController {
    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload-multiple")
    public ResponseEntity<Map<String, String>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        Map<String, String> uploadedFiles = new HashMap<>();

        for (MultipartFile file : files) {
            try {
                String fileId = storageService.upload(file);
                //uploadedFiles.put(file.getOriginalFilename(), fileId);
                uploadedFiles.put(fileId, StorageUtil.getDownloadUrl(request, file.getOriginalFilename()));
            } catch (Exception e) {
                uploadedFiles.put(file.getOriginalFilename(), "Failed: " + e.getMessage());
            }
        }

        return ResponseEntity.ok(uploadedFiles);
    }
}
