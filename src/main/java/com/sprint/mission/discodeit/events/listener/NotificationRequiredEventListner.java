package com.sprint.mission.discodeit.events.listener;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.events.MessageCreatedEvent;
import com.sprint.mission.discodeit.events.RoleUpdatedEvent;
import com.sprint.mission.discodeit.events.UploadFailedEvent;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationRequiredEventListner {

    private final NotificationService notificationService;
    private final ReadStatusService readStatusService;
    private final UserService userService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(MessageCreatedEvent event) {

        //public 채널 생성시에는 read status가 없고, 요구사항이 명확하지 않아 임의로 구현
        //public 채널에는 모든 유저가 포함되어 있다고 간주.
        //public 채널 메세지의 경우, 모든 유저 중, read-status 가 있는 유저는 알림여부를 확인하고
        //없는 유저는 public 채널의 알림여부 기본값이 false 이므로 보내지 않음.
        //즉, read-status가 있는 유저 중, 채널 타입에 관계 없이 알림여부에 따라 발송 하면됨

        List<Notification> notificationList = readStatusService.findAllByChannelId(event.getChannel().getId())
                .stream()
                .filter(x -> x.userId() != event.getAuthorId())
                .map(x -> Notification.builder()
                        .title(event.getTitle())
                        .content(event.getContent())
                        .receiverId(x.userId())
                        .build()).toList();

        notificationService.saveAllNotifications(notificationList);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(RoleUpdatedEvent event) {
        Notification notification = Notification.builder()
                .title(event.getTitle())
                .content(event.getContent())
                .receiverId(event.getReceiverId()).build();

        notificationService.saveNotification(notification);
    }

    @Async
    public void on(UploadFailedEvent event) {
        List<UserDto> admin = userService.findAllByRole(Role.ADMIN);
        if (admin == null || admin.size() == 0) {
            return;
        }

        List<Notification> notifications = admin.stream().map(x -> Notification.builder()
                .title(event.getTitle())
                .content(event.getContent())
                .receiverId(x.id()).build()).toList();

        notificationService.saveAllNotifications(notifications);
    }
}
