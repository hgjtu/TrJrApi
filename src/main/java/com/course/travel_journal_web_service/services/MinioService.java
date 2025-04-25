package com.course.travel_journal_web_service.services;

import com.course.travel_journal_web_service.CustomExceptions.StorageUnavailableException;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@Service
public class MinioService implements HealthIndicator {

    @Autowired
    private MinioClient minioClient;

    @Value("${spring.minio.url}")
    private String endpoint;

    @Value("${spring.minio.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws Exception {
        String fileName = generateFileName(file.getOriginalFilename());

        if(health().getStatus().equals(Status.UP)){
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build());
                return fileName;
            }
            catch (Exception e) {
                throw new RuntimeException("Ошибка загрузки файла в MinIO", e);
            }
        }
        else {
            throw new StorageUnavailableException("Файловое хранилище недоступно недоступно");
        }

    }

    public Resource getFile(String filename) {
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );
            return new InputStreamResource(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении файла из MinIO", e);
        }
    }

    public String getFileAsBase64(String filename) {
        try {
            if(fileExists(filename)){
                String contentType = getContentType(filename);

                InputStream inputStream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucketName)
                                .object(filename)
                                .build());

                byte[] bytes = IOUtils.toByteArray(inputStream);
                return "data:" + contentType + ";base64,"
                        + Base64.getEncoder().encodeToString(bytes);
            }
            else return "";

        } catch (Exception e) {
            throw new RuntimeException("Error converting file to Base64", e);
        }
    }

    public String getContentType(String filename) {
        String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        return switch (ext) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            default -> "application/octet-stream";
        };
    }

    public void deleteFile(String fileName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build());
    }

    public boolean fileExists(String filename) {
        try {
            if(health().getStatus().equals(Status.UP)) {
                minioClient.statObject(
                        StatObjectArgs.builder()
                                .bucket(bucketName)
                                .object(filename)
                                .build()
                );
                return true;
            }
            else return false;
        }
        catch (Exception e) {
            if (e instanceof ErrorResponseException errorResponse
                    && errorResponse.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            throw new RuntimeException("Ошибка при проверке существования файла в MinIO", e);
        }
    }

    public String getFileUrl(String fileName) {
        return String.format("%s/%s/%s", endpoint, bucketName, fileName);
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "-" + originalFileName;
    }

    @Override
    public Health health() {
        try {
            // Простая проверка - список bucket'ов
            minioClient.listBuckets();
            return Health.up().withDetail("message", "MinIO is available").build();
        } catch (Exception e) {
            return Health.down().withDetail("error", e.getMessage()).build();
        }
    }
}