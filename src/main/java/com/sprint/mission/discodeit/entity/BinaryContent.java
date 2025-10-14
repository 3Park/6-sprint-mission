package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter(value = AccessLevel.PROTECTED)
@Getter
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name")
  private String fileName;

  private Long size;

  @Column(name = "content_type")
  private String contentType;

  public BinaryContent() {
    this.id = UUID.randomUUID();
  }

  public BinaryContent(String fileName, Long size, String contentType) {
    this.id = UUID.randomUUID();
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }
}
