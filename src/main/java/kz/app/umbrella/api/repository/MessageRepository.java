package kz.app.umbrella.api.repository;

import kz.app.umbrella.api.entity.ApiUser;
import kz.app.umbrella.api.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findAllByUser(ApiUser user);
}
