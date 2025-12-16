package com.file.vault.controller;

import com.file.vault.cache.BinaryCacheService;
import com.file.vault.entity.FileMetadata;
import com.file.vault.repository.FileMetadataRepository;
import com.file.vault.client.MiniOClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@RestController
public class DownloadController {

    private final FileMetadataRepository fileMetadataRepository;

    private final MiniOClient miniOClient;

    private final BinaryCacheService binaryCacheService;

    public DownloadController(@Autowired FileMetadataRepository fileMetadataRepository,
                              @Autowired MiniOClient miniOClient,
                              @Autowired BinaryCacheService binaryCacheService) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.miniOClient = miniOClient;
        this.binaryCacheService = binaryCacheService;
    }

    @GetMapping(value = "/documents/{id}")
    public ResponseEntity<?> download(@PathVariable("id") String id) throws Exception {
        UUID uuid = UUID.fromString(id);

        byte[] data = binaryCacheService.getCache(uuid);
        if (data != null){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+id+"\"")
                    .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                    .body(data);
        }

        Optional<FileMetadata> result = fileMetadataRepository.findById(uuid);

        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        try {
            InputStream inputStream = miniOClient.download(uuid);
            data = inputStream.readAllBytes();
            binaryCacheService.saveCache(uuid, data);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+id+"\"")
                    .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                    .body(data);
        } catch (NoSuchKeyException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
