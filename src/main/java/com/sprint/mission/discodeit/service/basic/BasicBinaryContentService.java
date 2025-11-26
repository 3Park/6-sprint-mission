package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.custom.binary.BinaryNotFoundException;
import com.sprint.mission.discodeit.exception.errorcode.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional(readOnly = true)
  public BinaryContentDto find(UUID binaryContentId) {
    BinaryContent content = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new BinaryNotFoundException(ErrorCode.INVALID_BINARY_DATA,
            Map.of("id", binaryContentId)));

    log.info("download BinaryContent with id : {}", binaryContentId);

    return BinaryContentMapper.INSTANCE.toDto(content);
  }

  @Override
  @Transactional(readOnly = true)

  public List<BinaryContentDto> findAllByIdIn(
      List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIdIn(binaryContentIds).orElseThrow()
        .stream().map(BinaryContentMapper.INSTANCE::toDto).toList();
  }

  @Override
  public void delete(UUID binaryContentId) {
    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw new BinaryNotFoundException(ErrorCode.INVALID_BINARY_DATA,
          Map.of("id", binaryContentId));
    }

    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new BinaryNotFoundException(ErrorCode.INVALID_BINARY_DATA,
            Map.of("id", binaryContentId)
        ));

    binaryContentRepository.deleteById(binaryContentId);
    binaryContentStorage.delete(binaryContentId, binaryContent.getContentType());
  }
}
