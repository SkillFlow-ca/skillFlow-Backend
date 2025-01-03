package com.skillflow.skillflowbackend.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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
                            .contentType("video/mp4") // Set correct Content-Type
                            .build(),
                    file.toPath());
            return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }
    public String uploadStream(InputStream inputStream, String fileName) {
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType("video/mp4") // Set correct Content-Type
                            .build(),
                    RequestBody.fromInputStream(inputStream, inputStream.available()));
            return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
        } catch (S3Exception | IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }
    public String uploadStream2(InputStream inputStream, String key) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create("AKIA23WHTY2SJJ3MSWUR", "6xtwvBeRppE4ff/+cAIuAyGdQrkv5/cDuGudyzfJ");
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.CA_CENTRAL_1;

        S3Client s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        CreateMultipartUploadRequest createRequest = CreateMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();


        CreateMultipartUploadResponse createResponse = s3.createMultipartUpload(createRequest);
        String uploadId = createResponse.uploadId();
        List<CompletedPart> completedParts = new ArrayList<>();
        int partNumber = 1;
        ByteBuffer buffer = ByteBuffer.allocate(5 * 1024 * 1024);
        try (RandomAccessFile file = new RandomAccessFile("vid/", "r")) {
            long fileSize = file.length();
            long position = 0;

            while (position < fileSize) {
                file.seek(position);
                int bytesRead = file.getChannel().read(buffer);

                buffer.flip();
                UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .uploadId(uploadId)
                        .partNumber(partNumber)
                        .contentLength((long) bytesRead)
                        .build();

                UploadPartResponse response = s3.uploadPart(uploadPartRequest, RequestBody.fromByteBuffer(buffer));

                completedParts.add(CompletedPart.builder()
                        .partNumber(partNumber)
                        .eTag(response.eTag())
                        .build());

                buffer.clear();
                position += bytesRead;
                partNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Complete the multipart upload
        CompletedMultipartUpload completedUpload = CompletedMultipartUpload.builder()
                .parts(completedParts)
                .build();

        CompleteMultipartUploadRequest completeRequest = CompleteMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(key)
                .uploadId(uploadId)
                .multipartUpload(completedUpload)
                .build();

        //CompleteMultipartUploadResponse completeResponse = s3.completeMultipartUpload(completeRequest);

        // Print the object's URL
        String objectUrl = s3.utilities().getUrl(GetUrlRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build())
                .toExternalForm();

        System.out.println("Uploaded object URL: " + objectUrl);
        return objectUrl;
    }
}
