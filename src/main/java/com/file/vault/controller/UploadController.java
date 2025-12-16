package com.file.vault.controller;

import com.file.vault.entity.FileMetadata;
import com.file.vault.helper.PdfValidationHelper;
import com.file.vault.repository.FileMetadataRepository;
import com.file.vault.client.MiniOClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class UploadController {

    private final FileMetadataRepository fileMetadataRepository;

    private final MiniOClient miniOClient;

    private final PdfValidationHelper pdfValidationHelper;

    public UploadController(@Autowired FileMetadataRepository fileMetadataRepository,
                            @Autowired MiniOClient miniOClient,
                            @Autowired PdfValidationHelper pdfValidationHelper){
        this.fileMetadataRepository = fileMetadataRepository;
        this.miniOClient = miniOClient;
        this.pdfValidationHelper = pdfValidationHelper;
    }

    @PostMapping(value = "/documents", consumes = "multipart/form-data")
    public ResponseEntity<UploadController.Response> upload(@RequestPart("file") MultipartFile file) throws Exception{

        if (file.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new UploadController.Response("file is empty"));
        }

        if (!pdfValidationHelper.isPdf(file)){
            return ResponseEntity
                    .badRequest()
                    .body(new UploadController.Response("not valid pdf file"));
        }

        UUID uuid = UUID.randomUUID();

        FileMetadata fileMetadata = new FileMetadata(uuid, file.getSize());

        miniOClient.upload(uuid, file.getInputStream(), file.getSize());

        try {
            fileMetadataRepository.save(fileMetadata);
        } catch (Exception e){
            miniOClient.delete(uuid);
            throw new Exception();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UploadController.Response("file uploaded"));
    }

    @AllArgsConstructor
    @Getter
    static class Response {
        private final String message;
    }
}




