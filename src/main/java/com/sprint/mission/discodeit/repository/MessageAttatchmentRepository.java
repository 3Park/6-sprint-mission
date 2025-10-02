package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.MessageAttatchment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageAttatchmentRepository extends JpaRepository<MessageAttatchment, UUID> {

}
