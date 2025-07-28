package com.trustai.storage_service.util;

import jakarta.servlet.http.HttpServletRequest;

public class StorageUtil {
    public static final String DOWNLOAD_PATH = "/api/v1/files/download/";

    public static String getDownloadUrl(HttpServletRequest request, String fileName) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return baseUrl + DOWNLOAD_PATH + fileName;
    }
}
