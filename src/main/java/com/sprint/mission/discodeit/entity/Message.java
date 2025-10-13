package com.sprint.mission.discodeit.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter(value = AccessLevel.PROTECTED)
@Getter
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

  @Column
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id")
  private Channel channel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  private User author;

  @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MessageAttatchment> attachments;

  public Message() {
    this.id = UUID.randomUUID();
  }

  public Message(String content, Channel channel, User author) {
    this.id = UUID.randomUUID();
    this.content = content;
    this.channel = channel;
    this.author = author;
  }

  public void update(String newContent) {
    this.content = newContent;
  }

  public void setAttatchments(List<MessageAttatchment> attatchments) {
    this.attachments = attatchments;
  }
}
