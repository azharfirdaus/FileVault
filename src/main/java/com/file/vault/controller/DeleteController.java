package com.file.vault.controller;

import com.file.vault.cache.BinaryCacheService;
import com.file.vault.entity.FileMetadata;
import com.file.vault.repository.FileMetadataRepository;
import com.file.vault.client.MiniOClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.util.Optional;
import java.util.UUID;

@RestController
public class DeleteController {

    private final FileMetadataRepository fileMetadataRepository;

    private final MiniOClient miniOClient;

    private final BinaryCacheService binaryCacheService;

    public DeleteController(@Autowired FileMetadataRepository fileMetadataRepository,
                            @Autowired MiniOClient miniOClient,
                            @Autowired BinaryCacheService binaryCacheService) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.miniOClient = miniOClient;
        this.binaryCacheService = binaryCacheService;
    }

    @DeleteMapping(value = "/documents/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {

        UUID uuid = UUID.fromString(id);
        Optional<FileMetadata> result = fileMetadataRepository.findById(uuid);

        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            fileMetadataRepository.delete(result.get());

            try {
                miniOClient.delete(uuid);
            } catch (NoSuchKeyException e) {
                // do nothing
            }

            binaryCacheService.removeCacheAsync(uuid);
        }

        return ResponseEntity
                .ok()
                .build();
    }

}
