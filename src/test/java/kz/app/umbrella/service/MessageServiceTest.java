package kz.app.umbrella.service;

import kz.app.umbrella.api.entity.ApiRole;
import kz.app.umbrella.api.entity.ApiUser;
import kz.app.umbrella.api.entity.MessageEntity;
import kz.app.umbrella.api.exception.TelegramSendException;
import kz.app.umbrella.api.repository.MessageRepository;
import kz.app.umbrella.api.service.MessageService;
import kz.app.umbrella.api.service.TelegramBot;
import kz.app.umbrella.api.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserService userService;
    @Mock
    private TelegramBot telegramBot;

    @Test
    void testSendMessage_sendsToTelegramAndSaves() throws Exception {
        String username = "testuser";
        String text = "Hello!";

        ApiUser user = new ApiUser(1L, username, "pass", ApiRole.USER,
                Date.from(Instant.now()),"token", "123456");

        Mockito.when(userService.getUserByEmail(username))
                .thenReturn(user);

        messageService.sendMessage(username, text);

        Mockito.verify(telegramBot).execute(ArgumentMatchers.argThat((SendMessage msg) ->
                msg.getChatId().equals("123456") &&
                msg.getText().contains(text)
        ));

        Mockito.verify(messageRepository).save(Mockito.any(MessageEntity.class));
    }


    @Test
    void testSendMessage_withoutChatId_shouldThrow() {
        ApiUser user = new ApiUser(1L, "testuser", "pass", ApiRole.USER,
                Date.from(Instant.now()),"token", null);

        Mockito.when(userService.getUserByEmail("testuser")).thenReturn(user);

        Assertions.assertThrows(TelegramSendException.class, () -> {
            messageService.sendMessage("testuser", "hi");
        });
    }

    @Test
    void testGetUserMessages_shouldReturnList() {
        ApiUser user = new ApiUser();
        user.setEmail("bob");

        Mockito.when(userService.getUserByEmail("bob")).thenReturn(user);

        Mockito.when(messageRepository.findAllByUser(user)).thenReturn(List.of(
                new MessageEntity(null, "hi", Date.from(Instant.now()), user)
        ));

        var result = messageService.getUserMessages("bob");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("hi", result.get(0).text());
    }

}
