package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.utility.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.File;

@RestController
@RequestMapping("/api/videos")
public class TestController {
    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("video-", file.getOriginalFilename());
            file.transferTo(tempFile);

            String videoUrl = s3Service.uploadFile(tempFile, file.getOriginalFilename());
            return ResponseEntity.ok(videoUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }
}
