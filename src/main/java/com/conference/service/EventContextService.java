package com.conference.service;

import com.conference.domain.EventContext;
import com.conference.repository.EventContextRepository;
import com.conference.service.dto.EventContextDTO;
import com.conference.service.mapper.EventContextMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EventContext}.
 */
@Service
@Transactional
public class EventContextService {

    private final Logger log = LoggerFactory.getLogger(EventContextService.class);

    private final EventContextRepository eventContextRepository;

    private final EventContextMapper eventContextMapper;

    public EventContextService(EventContextRepository eventContextRepository, EventContextMapper eventContextMapper) {
        this.eventContextRepository = eventContextRepository;
        this.eventContextMapper = eventContextMapper;
    }

    /**
     * Save a eventContext.
     *
     * @param eventContextDTO the entity to save.
     * @return the persisted entity.
     */
    public EventContextDTO save(EventContextDTO eventContextDTO) {
        log.debug("Request to save EventContext : {}", eventContextDTO);
        EventContext eventContext = eventContextMapper.toEntity(eventContextDTO);
        eventContext = eventContextRepository.save(eventContext);
        return eventContextMapper.toDto(eventContext);
    }

    /**
     * Update a eventContext.
     *
     * @param eventContextDTO the entity to save.
     * @return the persisted entity.
     */
    public EventContextDTO update(EventContextDTO eventContextDTO) {
        log.debug("Request to update EventContext : {}", eventContextDTO);
        EventContext eventContext = eventContextMapper.toEntity(eventContextDTO);
        eventContext = eventContextRepository.save(eventContext);
        return eventContextMapper.toDto(eventContext);
    }

    /**
     * Partially update a eventContext.
     *
     * @param eventContextDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EventContextDTO> partialUpdate(EventContextDTO eventContextDTO) {
        log.debug("Request to partially update EventContext : {}", eventContextDTO);

        return eventContextRepository
            .findById(eventContextDTO.getId())
            .map(existingEventContext -> {
                eventContextMapper.partialUpdate(existingEventContext, eventContextDTO);

                return existingEventContext;
            })
            .map(eventContextRepository::save)
            .map(eventContextMapper::toDto);
    }

    /**
     * Get all the eventContexts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EventContextDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventContexts");
        return eventContextRepository.findAll(pageable).map(eventContextMapper::toDto);
    }

    /**
     * Get one eventContext by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventContextDTO> findOne(Long id) {
        log.debug("Request to get EventContext : {}", id);
        return eventContextRepository.findById(id).map(eventContextMapper::toDto);
    }

    /**
     * Delete the eventContext by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EventContext : {}", id);
        eventContextRepository.deleteById(id);
    }
}
