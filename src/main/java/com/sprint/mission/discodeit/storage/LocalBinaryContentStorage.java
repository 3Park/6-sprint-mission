package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
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
import java.util.UUID;
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
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] content) {

    Path path = resolvePath(binaryContentId);
    try (
        FileOutputStream fos = new FileOutputStream(path.toFile());
        BufferedOutputStream bs = new BufferedOutputStream(fos);
    ) {
      bs.write(content);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    Path path = resolvePath(binaryContentId);
    try {
      FileInputStream fos = new FileInputStream(path.toFile());
      BufferedInputStream bs = new BufferedInputStream(fos);
      return bs;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto dto) {

    if (dto == null) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    InputStream stream = get(dto.getId());
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
  public void delete(UUID binaryContentId) {
    Path path = resolvePath(binaryContentId);
    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Path resolvePath(UUID binaryContentId) {
    return root.resolve(binaryContentId.toString());
  }
}
