package com.learning.files;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class FileService {

    private static final List<String> ALLOWED_TYPES = List.of(
        "image/jpeg",
        "image/png",
        "image/gif",
        "image/webp",
        "video/mp4",
        "video/webm",
        "video/mpeg",
        "video/quicktime", 
        "video/x-msvideo"    
    );

    public String upload(MultipartFile file) {
        if(file.isEmpty()) throw new RuntimeException("This file is empty!");

        if(!ALLOWED_TYPES.contains(file.getContentType()) || file.getContentType() == null) throw new RuntimeException("file type is not supported!");

        try {
            String filePath = System.getProperty("user.home") + "/uploads";
            String fileName = LocalDateTime.now().getNano() + file.getOriginalFilename();
    
            File finalFile = new File(filePath, fileName);
            finalFile.getParentFile().mkdirs();
            file.transferTo(finalFile);

            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/{filename}")
                    .buildAndExpand(fileName)
                    .toUriString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
