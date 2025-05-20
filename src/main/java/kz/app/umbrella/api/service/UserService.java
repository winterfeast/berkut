package kz.app.umbrella.api.service;

import kz.app.umbrella.api.entity.ApiRole;
import kz.app.umbrella.api.entity.ApiUser;
import kz.app.umbrella.api.exception.UserAlreadyExistsException;
import kz.app.umbrella.api.repository.ApiUserRepository;
import kz.app.umbrella.dto.AuthRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
