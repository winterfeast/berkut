package kz.app.umbrella.bot.config;

import kz.app.umbrella.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final UserService userService;

    public TelegramBot(@Value("${telegram.bot.token}") String botToken,
                  @Value("${telegram.bot.username}") String botUsername,
                  UserService userService
    ) {
        super(botToken);
        this.botUsername = botUsername;
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Update received: updateId: {}", update.getUpdateId());

        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText().trim();
            String chatId = String.valueOf(update.getMessage().getChatId());

            if (text.matches("^[a-f0-9\\-]{36}$")) {
                userService.bindChatIdToUser(text, chatId);
                send(chatId, "Токен принят. Telegram привязан к вашему аккаунту.");
            } else {
                send(chatId, "Отправьте токен из вашего личного кабинета для привязки.");
            }
        }
    }

    private void send(String chatId, String messageText) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error while sending text {}",e.getMessage(), e);
        }
    }
}