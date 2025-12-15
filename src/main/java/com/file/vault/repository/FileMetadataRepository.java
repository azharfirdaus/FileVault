package com.file.vault.repository;

import com.file.vault.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, UUID> {
}
