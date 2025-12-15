package com.file.vault.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.File;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "file_metadata")
@Getter
public class FileMetadata {

    @Id
    @Column(name = "uuid", columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(name = "length", nullable = false)
    private long length;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected FileMetadata(){}

    public FileMetadata(UUID uuid, long length){
        this.uuid = uuid;
        this.length = length;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
