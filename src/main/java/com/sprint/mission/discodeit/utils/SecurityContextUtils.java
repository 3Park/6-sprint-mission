package com.sprint.mission.discodeit.utils;

import com.sprint.mission.discodeit.dto.data.DiscodeitUserDetails;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.notification.NotificationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("SecurityContext")
public class SecurityContextUtils {
    public UUID getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || authentication.getDetails() instanceof DiscodeitUserDetails)
            throw new NotificationException(ErrorCode.INVALID_USER_CREDENTIALS);

        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
        return userDetails.getUserId();
    }
}
