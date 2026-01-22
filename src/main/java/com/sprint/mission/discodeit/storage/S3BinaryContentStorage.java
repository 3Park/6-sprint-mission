package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.exception.custom.binary.BinaryBadContentTypeException;
import com.sprint.mission.discodeit.exception.custom.binary.BinaryIOException;
import com.sprint.mission.discodeit.exception.custom.binary.BinaryS3Exception;
import com.sprint.mission.discodeit.exception.custom.binary.EmptyBinaryRequestException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
    prefix = "discodeit.storage",
    name = "type",
    havingValue = "s3",
    matchIfMissing = false
)
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  @Value("${cloud.aws.s3.presigned-url-expiration}")
  private long duration;

  @Override
  public UUID put(UUID binaryContentId, byte[] content, String contentType) {

    try {
      if (content == null || content.length == 0) {
        throw new EmptyBinaryRequestException(ErrorCode.EMPTY_BINARY_REQUEST_EXCEPTION,
            Map.of("Id", binaryContentId));
      }

      String s3Key = generateS3Key(binaryContentId, contentType);

      RequestBody requestBody = RequestBody.fromBytes(content);
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(s3Key)
          .build();

      s3Client.putObject(putObjectRequest, requestBody);
      return binaryContentId;
    } catch (Exception e) {
      throw new BinaryS3Exception(ErrorCode.AWS_EXCEPTION,
          Map.of("message", e.getMessage(), "action", "upload"));
    }
  }

  @Override
  public InputStream get(UUID binaryContentId, String contentType) {
    String s3Key = generateS3Key(binaryContentId, contentType);

    try {
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucketName)
          .key(s3Key)
          .build();

      return s3Client.getObject(getObjectRequest);
    } catch (Exception e) {
      throw new BinaryS3Exception(ErrorCode.AWS_EXCEPTION,
          Map.of("message", e.getMessage(), "action", "get from S3"));
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto dto) {

    try {
      String s3Key = generateS3Key(dto.getId(), dto.getContentType());
      return ResponseEntity.status(HttpStatus.FOUND)
          .location(URI.create(generatePresignedUrl(s3Key)))
          .build();
    } catch (Exception e) {
      throw new BinaryS3Exception(ErrorCode.AWS_EXCEPTION,
          Map.of("message", e.getMessage(), "action", "download with presigned-url"));
    }
  }

  @Override
  public boolean delete(UUID binaryContentId, String contentType) {
    try {
      String s3Key = generateS3Key(binaryContentId, contentType);
      DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
          .bucket(bucketName)
          .key(s3Key)
          .build();
      s3Client.deleteObject(deleteObjectRequest);
      return true;
    } catch (Exception e) {
      throw new BinaryS3Exception(ErrorCode.AWS_EXCEPTION,
          Map.of("message", e.getMessage(), "action", "delete"));
    }
  }

  private String generateS3Key(UUID binaryContentId, String contentType) {
    if (contentType.isEmpty() || contentType.isBlank()) {
      throw new BinaryBadContentTypeException(ErrorCode.COMMON_EXCEPTION,
          Map.of("message", "Content type is empty"));
    }

    String[] extension = contentType.split("/");
    if (extension.length != 2) {
      throw new BinaryBadContentTypeException(ErrorCode.COMMON_EXCEPTION,
          Map.of("message", "Bad content type", "ContentType", contentType));
    }

    String s3Key = "files/" + binaryContentId.toString() + "." + extension[1];
    return s3Key;
  }

  private String generatePresignedUrl(String key) {

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
        .getObjectRequest(getObjectRequest)
        .signatureDuration(Duration.ofMinutes(duration))
        .build();

    return s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();
  }
}
