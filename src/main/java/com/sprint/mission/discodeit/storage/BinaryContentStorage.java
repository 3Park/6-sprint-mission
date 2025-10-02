package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

public interface BinaryContentStorage {

  UUID put(UUID binaryContentId, byte[] content);

  InputStream get(UUID binaryContentId);

  ResponseEntity<?> download(BinaryContentDto dto);

  void delete(UUID binaryContentId);
}
