package com.conference.service;

import com.conference.domain.EventRegistration;
import com.conference.repository.EventRegistrationRepository;
import com.conference.service.dto.EventRegistrationDTO;
import com.conference.service.mapper.EventRegistrationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EventRegistration}.
 */
@Service
@Transactional
public class EventRegistrationService {

    private final Logger log = LoggerFactory.getLogger(EventRegistrationService.class);

    private final EventRegistrationRepository eventRegistrationRepository;

    private final EventRegistrationMapper eventRegistrationMapper;

    public EventRegistrationService(
        EventRegistrationRepository eventRegistrationRepository,
        EventRegistrationMapper eventRegistrationMapper
    ) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.eventRegistrationMapper = eventRegistrationMapper;
    }

    /**
     * Save a eventRegistration.
     *
     * @param eventRegistrationDTO the entity to save.
     * @return the persisted entity.
     */
    public EventRegistrationDTO save(EventRegistrationDTO eventRegistrationDTO) {
        log.debug("Request to save EventRegistration : {}", eventRegistrationDTO);
        EventRegistration eventRegistration = eventRegistrationMapper.toEntity(eventRegistrationDTO);
        eventRegistration = eventRegistrationRepository.save(eventRegistration);
        return eventRegistrationMapper.toDto(eventRegistration);
    }

    /**
     * Update a eventRegistration.
     *
     * @param eventRegistrationDTO the entity to save.
     * @return the persisted entity.
     */
    public EventRegistrationDTO update(EventRegistrationDTO eventRegistrationDTO) {
        log.debug("Request to update EventRegistration : {}", eventRegistrationDTO);
        EventRegistration eventRegistration = eventRegistrationMapper.toEntity(eventRegistrationDTO);
        eventRegistration = eventRegistrationRepository.save(eventRegistration);
        return eventRegistrationMapper.toDto(eventRegistration);
    }

    /**
     * Partially update a eventRegistration.
     *
     * @param eventRegistrationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EventRegistrationDTO> partialUpdate(EventRegistrationDTO eventRegistrationDTO) {
        log.debug("Request to partially update EventRegistration : {}", eventRegistrationDTO);

        return eventRegistrationRepository
            .findById(eventRegistrationDTO.getId())
            .map(existingEventRegistration -> {
                eventRegistrationMapper.partialUpdate(existingEventRegistration, eventRegistrationDTO);

                return existingEventRegistration;
            })
            .map(eventRegistrationRepository::save)
            .map(eventRegistrationMapper::toDto);
    }

    /**
     * Get all the eventRegistrations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EventRegistrationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventRegistrations");
        return eventRegistrationRepository.findAll(pageable).map(eventRegistrationMapper::toDto);
    }

    /**
     * Get one eventRegistration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventRegistrationDTO> findOne(Long id) {
        log.debug("Request to get EventRegistration : {}", id);
        return eventRegistrationRepository.findById(id).map(eventRegistrationMapper::toDto);
    }

    /**
     * Delete the eventRegistration by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EventRegistration : {}", id);
        eventRegistrationRepository.deleteById(id);
    }
}
