package net.hiddenpass.hiddenpass.repository;

import net.hiddenpass.hiddenpass.enumerations.ERol;
import net.hiddenpass.hiddenpass.models.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRole(ERol role);
}
