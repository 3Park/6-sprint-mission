package com.sprint.mission.discodeit.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(value = "jwt")
@Getter
@Setter
public class JwtProperties {

  private String secret;
  private String issuer;
  private Long accessKeyExpiration;
  private Long refreshKeyExpiration;
}
