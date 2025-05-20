package kz.app.umbrella.api.dto;

import kz.app.umbrella.api.entity.MessageEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;

public record MessageResponse(LocalDateTime timestamp, String text) {

    public MessageResponse(MessageEntity entity) {
        this(entity.getCreatedAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(),
                entity.getText());
    }
}
