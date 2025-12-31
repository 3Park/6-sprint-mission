package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.dto.data.DiscodeitUserDetails;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("UserCheck")
public class UserIdCheckHelper {

  public UUID getUserId(Authentication authentication) {
    if (authentication == null ||
        !(authentication.getPrincipal() instanceof DiscodeitUserDetails user)) {
      return null;
    }

    return user.getUserId();
  }

}
