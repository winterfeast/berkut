package kz.app.umbrella.api.repository;

import kz.app.umbrella.api.entity.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiUserRepository extends JpaRepository<ApiUser, Long> {
    Optional<ApiUser> findByEmail(String email);

    Optional<ApiUser> findByTelegramToken(String telegramToken);
}
