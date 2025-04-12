package com.course.travel_journal_web_service.services;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${spring.minio.url}")
    private String endpoint;

    @Value("${spring.minio.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws Exception {
        String fileName = generateFileName(file.getOriginalFilename());

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
            String contentType = getContentType(filename);

            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build());

            byte[] bytes = IOUtils.toByteArray(inputStream);
            return "data:" + contentType + ";base64,"
                    + Base64.getEncoder().encodeToString(bytes);
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
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );
            return true;
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
}