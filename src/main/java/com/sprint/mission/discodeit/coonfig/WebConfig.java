package com.sprint.mission.discodeit.coonfig;

import com.sprint.mission.discodeit.log.ControllerLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  private ControllerLogInterceptor controllerLogInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(controllerLogInterceptor).addPathPatterns("/**")
        .excludePathPatterns("/css/**", "/js/**", "/images/**");
  }

}
