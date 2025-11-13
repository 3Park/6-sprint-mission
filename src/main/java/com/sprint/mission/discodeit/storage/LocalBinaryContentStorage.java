package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.exception.custom.binary.BinaryBadContentTypeException;
import com.sprint.mission.discodeit.exception.custom.binary.BinaryIOException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(
    prefix = "discodeit.storage",
    name = "type",
    havingValue = "local",
    matchIfMissing = false
)
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String root) {
    this.root = Paths.get(System.getProperty("user.dir"), root);
    init();
  }

  private void init() {
    if (Files.exists(root)) {
      return;
    }

    try {
      Files.createDirectory(root);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      log.debug(e.getMessage(), e.getStackTrace());
      throw new BinaryIOException(ErrorCode.COMMON_EXCEPTION, Map.of("message", e.getMessage()));
    }
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] content, String contentType) {
    Path path = resolvePath(binaryContentId, contentType);

    try (
        FileOutputStream fos = new FileOutputStream(path.toFile());
        BufferedOutputStream bs = new BufferedOutputStream(fos);
    ) {
      bs.write(content);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      log.debug(e.getMessage(), e.getStackTrace());
      throw new BinaryIOException(ErrorCode.COMMON_EXCEPTION, Map.of("message", e.getMessage()));
    }

    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId, String contentType) {
    Path path = resolvePath(binaryContentId, contentType);
    try (FileInputStream fos = new FileInputStream(path.toFile());
        BufferedInputStream bs = new BufferedInputStream(fos);) {
      return bs;
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      log.debug(e.getMessage(), e.getStackTrace());
      throw new BinaryIOException(ErrorCode.COMMON_EXCEPTION, Map.of("message", e.getMessage()));
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto dto) {

    if (dto == null) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    InputStream stream = get(dto.getId(), dto.getContentType());
    if (stream == null) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    return ResponseEntity.status(HttpStatus.OK)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + dto.getId() + ".png\"") // ★ 다운로드 강제
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(new InputStreamResource(stream));
  }

  @Override
  public boolean delete(UUID binaryContentId, String contentType) {
    Path path = resolvePath(binaryContentId, contentType);
    try {
      Files.deleteIfExists(path);
      return true;
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      log.debug(e.getMessage(), e.getStackTrace());
      throw new BinaryIOException(ErrorCode.COMMON_EXCEPTION, Map.of("message", e.getMessage()));
    }
  }

  private Path resolvePath(UUID binaryContentId) {
    return root.resolve(binaryContentId.toString());
  }

  private Path resolvePath(UUID binaryContentId, String contentType) {
    if (contentType.isEmpty() || contentType.isBlank()) {
      throw new BinaryBadContentTypeException(ErrorCode.COMMON_EXCEPTION,
          Map.of("message", "Content type is empty"));
    }

    String[] extension = contentType.split("/");
    if (extension.length != 2) {
      throw new BinaryBadContentTypeException(ErrorCode.COMMON_EXCEPTION,
          Map.of("message", "Bad content type", "ContentType", contentType));
    }

    Path path = resolvePath(binaryContentId);
    return path.resolve("." + extension[1]);
  }
}
