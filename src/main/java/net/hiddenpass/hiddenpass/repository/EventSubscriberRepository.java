package net.hiddenpass.hiddenpass.repository;

import net.hiddenpass.hiddenpass.models.EventSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventSubscriberRepository extends JpaRepository<EventSubscriber, Long> {
}
