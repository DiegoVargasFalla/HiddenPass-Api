package net.hiddenpass.hiddenpass.repository;

import net.hiddenpass.hiddenpass.models.PassWordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PasswordRepository extends JpaRepository<PassWordEntity, Long> {
}
