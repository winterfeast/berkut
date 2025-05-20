package kz.app.umbrella.api.service;

import kz.app.umbrella.api.dto.MessageResponse;
import kz.app.umbrella.api.entity.ApiUser;
import kz.app.umbrella.api.entity.MessageEntity;
import kz.app.umbrella.api.exception.TelegramSendException;
import kz.app.umbrella.api.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final TelegramBot telegramBot;

    public void sendMessage(String email, String text) {
        ApiUser user = userService.getUserByEmail(email);

        if (user.getChatId() == null) {
            throw new TelegramSendException("User has not linked Telegram");
        }

        String formatted = user.getEmail() + ", я получил от тебя сообщение:\n" + text;

        SendMessage message = SendMessage.builder()
                .chatId(user.getChatId())
                .text(formatted)
                .build();
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramSendException("Failed to send message to Telegram", e);
        }

        MessageEntity saved = new MessageEntity();
        saved.setText(text);
        saved.setUser(user);

        messageRepository.save(saved);
    }

    public List<MessageResponse> getUserMessages(String email) {
        ApiUser user = userService.getUserByEmail(email);
        return messageRepository.findAllByUser(user).stream()
                .map(MessageResponse::new)
                .toList();
    }
}
