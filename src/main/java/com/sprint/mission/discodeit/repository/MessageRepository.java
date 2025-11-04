package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

  @Query("SELECT m FROM Message m JOIN FETCH m.channel WHERE m.channel.id = :channelId")
  Slice<Message> findAllByChannelId(@Param("channelId") UUID channelId, Pageable pageable);

  @Query("""
          SELECT m FROM Message m
          JOIN FETCH m.channel
          WHERE m.channel.id = :channelId
            AND m.createdAt > :createdAt
      """)
  Slice<Message> findSliceAllByChannelIdAndCreatedAtAfter(@Param("channelId") UUID channelId,
      @Param("createdAt") Instant createdAt,
      Pageable pageable);

  boolean existsById(UUID id);

  void deleteById(UUID id);

  void deleteAllByChannelId(UUID channelId);
}
