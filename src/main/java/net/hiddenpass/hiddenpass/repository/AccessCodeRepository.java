package net.hiddenpass.hiddenpass.repository;

import net.hiddenpass.hiddenpass.models.AccessCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessCodeRepository extends JpaRepository<AccessCodeEntity, Long> {}
