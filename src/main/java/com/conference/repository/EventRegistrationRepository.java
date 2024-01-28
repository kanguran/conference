package com.conference.repository;

import com.conference.domain.EventRegistration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventRegistration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    Page<EventRegistration> findByEventCounterpartyAppUserLogin(Pageable pageable, String login);
}
