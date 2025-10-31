package net.hiddenpass.hiddenpass.repository;

import net.hiddenpass.hiddenpass.models.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String email);
    boolean existsByUsername(String username);
}
