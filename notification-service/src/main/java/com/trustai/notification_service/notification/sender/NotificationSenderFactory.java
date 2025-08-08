package com.trustai.notification_service.notification.sender;

import com.trustai.notification_service.notification.enums.NotificationChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NotificationSenderFactory {
    private final Map<NotificationChannel, NotificationSender> senderMap;

    public NotificationSenderFactory(List<NotificationSender> senders) {
        this.senderMap = senders.stream()
                .collect(Collectors.toMap(NotificationSender::getChannel, Function.identity()));
    }

    public NotificationSender getSender(NotificationChannel channel) {
        return senderMap.get(channel);
    }
}
