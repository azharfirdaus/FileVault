package com.file.vault.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ListController {

    @GetMapping(value = "/documents")
    public ResponseEntity<List<Response>> list(@PathVariable("id") String id) throws Exception {
        Path filePath = Paths.get("UPLOAD_DIR").resolve(id).normalize();

        InputStreamResource resource =
                new InputStreamResource(Files.newInputStream(filePath));


        List<Response> fileDescriptionList = new ArrayList<>();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileDescriptionList);

    }

    @AllArgsConstructor
    @Getter
    static class Response {

        private String id;
        private long length;

    }
}


