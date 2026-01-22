package com.sprint.mission.discodeit.storage.s3;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Component
@Slf4j
@RequiredArgsConstructor
public class AWSS3Test {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  @Value("${cloud.aws.s3.presigned-url-expiration}")
  private long duration;

  private final String testFile = "HELP.md";
  private final String s3Key = "files" + "/" + testFile;

  public boolean upload() {
    //
    Path path = Paths.get(System.getProperty("user.dir"), testFile);
    try (FileInputStream fos = new FileInputStream(path.toFile())) {
      RequestBody requestBody = RequestBody.fromBytes(fos.readAllBytes());
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(s3Key)
          .build();

      s3Client.putObject(putObjectRequest, requestBody);
      return true;
    } catch (IOException e) {
      log.error("get file error : {}", e.getMessage());
      return false;
    }
  }

  public byte[] download() {
    try {
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucketName)
          .key(s3Key)
          .build();

      byte[] downloads = s3Client.getObject(getObjectRequest).readAllBytes();
      return downloads;
    } catch (Exception e) {
      log.error("get file error : {}", e.getMessage());
      return null;
    }
  }

  public String createPresignedUrl() {

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(s3Key)
        .build();

    GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
        .getObjectRequest(getObjectRequest)
        .signatureDuration(Duration.ofMinutes(duration))
        .build();

    return s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();
  }

}
