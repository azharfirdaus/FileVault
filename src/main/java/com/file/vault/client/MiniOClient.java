package com.file.vault.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.util.UUID;

@Service
public class MiniOClient {

    private final S3Client s3Client;
    private final String bucket;

    public MiniOClient(S3Client s3Client,
                       @Value("${minio.bucket}") String bucket){
        this.s3Client = s3Client;
        this.bucket = bucket;
    }

    public void upload(UUID id, InputStream inputStream, long size){
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(id.toString())
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromInputStream(inputStream, size)
        );

    }

    public InputStream download(UUID id){
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(id.toString())
                .build();

        return s3Client.getObject(request);
    }

    public void delete(UUID id) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(id.toString())
                .build());
    }

}
