package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter(value = AccessLevel.PROTECTED)
@Getter
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  private ChannelType type;

  private String name;
  private String description;

  public Channel() {
    this.id = UUID.randomUUID();
  }

  public Channel(ChannelType type, String name, String description) {
    this.id = UUID.randomUUID();
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    this.name = newName;
    this.description = newDescription;
  }
}
