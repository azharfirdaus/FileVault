package com.file.vault.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class DownloadController {

    @GetMapping(value = "/documents/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") String id) throws Exception {
        Path filePath = Paths.get("UPLOAD_DIR").resolve(id).normalize();

        InputStreamResource resource =
                new InputStreamResource(Files.newInputStream(filePath));

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"\"")
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(resource);
    }

}
