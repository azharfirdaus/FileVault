package com.file.vault.controller;

import com.file.vault.entity.FileMetadata;
import com.file.vault.repository.FileMetadataRepository;
import com.file.vault.repository.ObjectStorageService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RestController
public class ListController {

    private final FileMetadataRepository fileMetadataRepository;

    public ListController(@Autowired FileMetadataRepository fileMetadataRepository) {
        this.fileMetadataRepository = fileMetadataRepository;
    }

    @GetMapping(value = "/documents", produces = "application/json")
    public ResponseEntity<List<Response>> list() throws Exception {
        List<FileMetadata> fileMetadataList = fileMetadataRepository.findAll();

        List<Response> responses = fileMetadataList.stream()
                .map(fileMetadata -> new Response(fileMetadata.getUuid().toString(), fileMetadata.getLength(), fileMetadata.getCreatedAt()))
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);

    }

    @AllArgsConstructor
    @Getter
    static class Response {

        private String id;
        private long length;
        private Instant createdAt;

    }
}


