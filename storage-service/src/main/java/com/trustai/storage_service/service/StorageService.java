package com.trustai.storage_service.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface StorageService {
    List<String> listAllFiles();
    String upload(MultipartFile file);
    String upload(File file);
    InputStream  download(String fileId);
    void delete(String fileId);
}
