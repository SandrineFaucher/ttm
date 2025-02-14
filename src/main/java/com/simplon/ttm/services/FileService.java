package com.simplon.ttm.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String saveFile(MultipartFile file, String subDirectory) throws IOException;
}
