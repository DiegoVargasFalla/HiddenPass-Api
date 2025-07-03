package net.hiddenpass.hiddenpass.repository;

import net.hiddenpass.hiddenpass.models.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<NoteEntity, Long> {
}
