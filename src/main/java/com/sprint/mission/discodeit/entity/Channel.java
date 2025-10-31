package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter(value = AccessLevel.PROTECTED)
@Getter
@Table(name = "channels")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  private ChannelType type;

  private String name;
  private String description;

  public Channel(ChannelType type, String name, String description) {

    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    this.name = newName;
    this.description = newDescription;
  }
}
