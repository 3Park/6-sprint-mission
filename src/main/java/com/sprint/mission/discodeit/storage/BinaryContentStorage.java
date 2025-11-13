package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

public interface BinaryContentStorage {

  UUID put(UUID binaryContentId, byte[] content, String contentType);

  InputStream get(UUID binaryContentId, String contentType);

  ResponseEntity<?> download(BinaryContentDto dto);

  boolean delete(UUID binaryContentId, String contentType);
}
