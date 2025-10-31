package com.sprint.mission.discodeit;

import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ActiveProfileInfo implements InfoContributor {

  private final Environment environment;

  public ActiveProfileInfo(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void contribute(Builder builder) {
    builder.withDetail("애플리케이션 이름", environment.getProperty("spring.application.name"));
    builder.withDetail("애플리케이션 버전", environment.getProperty("app.version"));
    builder.withDetail("자바 버전", System.getProperty("java.version"));
    builder.withDetail("스프링 부트 버전", SpringBootVersion.getVersion());
    builder.withDetail("데이터소스 url", environment.getProperty("spring.datasource.url"));
    builder.withDetail("데이터소스 드라이버 클래스 이름",
        environment.getProperty("spring.datasource.driver-class-name"));
    builder.withDetail("jpa ddl-auto", environment.getProperty("spring.jpa.hibernate.ddl-auto"));
    builder.withDetail("storage 설정 type", environment.getProperty("discodeit.storage.type"));
    builder.withDetail("storage 설정 path",
        environment.getProperty("discodeit.storage.local.root-path"));
    builder.withDetail("multipart 설정 max-file-size",
        environment.getProperty("spring.servlet.multipart.max-file-size"));
    builder.withDetail("multipart 설정 max-request-size",
        environment.getProperty("spring.servlet.multipart.max-request-size"));
  }
}
