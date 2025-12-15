package com.file.vault.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class DeleteController {

    @DeleteMapping(value = "/documents/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        return ResponseEntity
                .noContent()
                .build();
    }

}
