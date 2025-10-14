package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {


  @Query("SELECT m FROM ReadStatus m JOIN FETCH m.user WHERE m.user.id = :userId")
  Optional<List<ReadStatus>> findAllByUserId(@Param("userId") UUID userId);

  @Query("SELECT m FROM ReadStatus m JOIN FETCH m.channel WHERE m.channel.id = :channelId")
  Optional<List<ReadStatus>> findAllByChannelId(@Param("channelId") UUID channelId);

  boolean existsById(UUID id);

  void deleteById(UUID id);

  void deleteAllByChannelId(UUID channelId);
}
