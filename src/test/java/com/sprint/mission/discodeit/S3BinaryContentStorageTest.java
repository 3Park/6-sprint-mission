package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.S3BinaryContentStorage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class S3BinaryContentStorageTest {

  @Autowired
  private S3BinaryContentStorage s3BinaryContentStorage;

  final String contentType = "text/md";
  static UUID uuid = UUID.randomUUID();

  @Test
  @Order(1)
  @DisplayName("AWS S3 업로드 테스트")
  void awsUploadTest() throws IOException {

    Path path = Paths.get(System.getProperty("user.dir"), "HELP.md");

    FileInputStream fos = new FileInputStream(path.toFile());
    byte[] fileContent = fos.readAllBytes();
    UUID id = s3BinaryContentStorage.put(uuid, fileContent, contentType);
    assertThat(id).isNotNull();
    assertThat(id).isEqualTo(uuid);
    fos.close();
  }

  @Test
  @Order(2)
  @DisplayName("AWS S3 다운로드 테스트")
  void awsDownloadTest() throws IOException {
    InputStream steamResult = s3BinaryContentStorage.get(uuid, contentType);
    assertThat(steamResult).isNotNull();
    assertThat(steamResult.readAllBytes()).isNotEmpty();
  }

  @Test
  @Order(3)
  @DisplayName("AWS S3 PresignedUrl 테스트")
  void awsPresignedUrlTest() {
    BinaryContentDto dto = new BinaryContentDto();
    dto.setContentType(contentType);
    dto.setId(uuid);
    ResponseEntity<?> response = s3BinaryContentStorage.download(dto);
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
  }

  @Test
  @Order(4)
  @DisplayName("AWS S3 항목 삭제 테스트")
  void awsS3DeleteTest() {
    boolean result = s3BinaryContentStorage.delete(uuid, contentType);
    assertThat(result).isTrue();
  }
}
