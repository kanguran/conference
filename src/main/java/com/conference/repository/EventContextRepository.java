package com.conference.repository;

import com.conference.domain.EventContext;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventContext entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventContextRepository extends JpaRepository<EventContext, Long> {}
