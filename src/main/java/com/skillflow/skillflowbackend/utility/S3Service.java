package com.skillflow.skillflowbackend.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.net.URL;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = "course-videos-skillflow";




    public S3Service( @Value("${aws.s3.access-key}") String accessKey,
                      @Value("${aws.s3.secret-key}") String secretKey,
                      @Value("${aws.s3.region}") String region) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public String uploadFile(File file, String fileName) {
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build(),
                    file.toPath());
            return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }
}
