package kz.app.umbrella.api.service;

import kz.app.umbrella.api.entity.ApiRole;
import kz.app.umbrella.api.entity.ApiUser;
import kz.app.umbrella.api.exception.UserAlreadyExistsException;
import kz.app.umbrella.api.exception.UserNotFoundException;
import kz.app.umbrella.api.repository.ApiUserRepository;
import kz.app.umbrella.api.dto.AuthRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final ApiUserRepository apiUserRepository;

    private final PasswordEncoder passwordEncoder;

    public void save(AuthRequest registerDto) {
        apiUserRepository.findByEmail(registerDto.email())
            .ifPresent(user -> {
                throw new UserAlreadyExistsException(registerDto.email());
            });

        ApiUser user = new ApiUser();
        user.setEmail(registerDto.email());
        user.setPassword(passwordEncoder.encode(registerDto.password()));
        user.setRole(ApiRole.USER);

        apiUserRepository.save(user);
        log.info("User with email {} saved", registerDto.email());
    }

    public String generateTelegramTokenForUser(String email) {
        String token = UUID.randomUUID().toString();
        ApiUser apiUser = apiUserRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        apiUser.setTelegramToken(token);
        apiUserRepository.save(apiUser);
        log.info("Telegram token for user {} saved", email);
        return token;
    }

    public void bindChatIdToUser(String token, String chatId) {
        apiUserRepository.findByTelegramToken(token)
            .ifPresent(user -> {
                user.setChatId(chatId);
                apiUserRepository.save(user);
                log.info("User with chatId {} bound to telegram", chatId);
            });
    }
}
