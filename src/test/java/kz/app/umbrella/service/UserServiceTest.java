package kz.app.umbrella.service;

import kz.app.umbrella.api.dto.AuthRequest;
import kz.app.umbrella.api.entity.ApiRole;
import kz.app.umbrella.api.entity.ApiUser;
import kz.app.umbrella.api.exception.UserAlreadyExistsException;
import kz.app.umbrella.api.exception.UserNotFoundException;
import kz.app.umbrella.api.repository.ApiUserRepository;
import kz.app.umbrella.api.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private ApiUserRepository apiUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void save_shouldCreateUserIfNotExists() {
        AuthRequest dto = new AuthRequest("test@mail.com", "pass123");
        Mockito.when(apiUserRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode("pass123")).thenReturn("hashed");

        userService.save(dto);

        ArgumentCaptor<ApiUser> captor = ArgumentCaptor.forClass(ApiUser.class);
        Mockito.verify(apiUserRepository).save(captor.capture());

        ApiUser saved = captor.getValue();
        Assertions.assertEquals("test@mail.com", saved.getEmail());
        Assertions.assertEquals("hashed", saved.getPassword());
        Assertions.assertEquals(ApiRole.USER, saved.getRole());
    }


    @Test
    void save_shouldThrowIfUserExists() {
        AuthRequest dto = new AuthRequest("existing@mail.com", "pass");
        Mockito.when(apiUserRepository.findByEmail("existing@mail.com"))
                .thenReturn(Optional.of(new ApiUser()));

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.save(dto));
        Mockito.verify(apiUserRepository, Mockito.never()).save(Mockito.any());
    }
    @Test
    void generateTelegramTokenForUser_shouldSaveToken() {
        String email = "a@b.com";
        ApiUser user = new ApiUser();
        Mockito.when(apiUserRepository.findByEmail(email)).thenReturn(Optional.of(user));

        String token = userService.generateTelegramTokenForUser(email);

        Assertions.assertNotNull(token);
        Assertions.assertEquals(token, user.getTelegramToken());
        Mockito.verify(apiUserRepository).save(user);
    }

    @Test
    void bindChatIdToUser_shouldSaveChatId() {
        String token = UUID.randomUUID().toString();
        ApiUser user = new ApiUser();
        Mockito.when(apiUserRepository.findByTelegramToken(token)).thenReturn(Optional.of(user));

        userService.bindChatIdToUser(token, "123456");

        Assertions.assertEquals("123456", user.getChatId());
        Mockito.verify(apiUserRepository).save(user);
    }

    @Test
    void getUserByEmail_shouldReturnUser() {
        String email = "email@mail.com";
        ApiUser user = new ApiUser();
        Mockito.when(apiUserRepository.findByEmail(email)).thenReturn(Optional.of(user));

        ApiUser result = userService.getUserByEmail(email);

        Assertions.assertEquals(user, result);
    }

    @Test
    void getUserByEmail_shouldThrowIfNotFound() {
        Mockito.when(apiUserRepository.findByEmail("missing@mail.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () ->
                userService.getUserByEmail("missing@mail.com")
        );
    }
}
