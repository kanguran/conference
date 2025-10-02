package com.conference.web.rest;

import com.conference.repository.EventRegistrationRepository;
import com.conference.service.EventRegistrationService;
import com.conference.service.dto.EventRegistrationDTO;
import com.conference.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.conference.domain.EventRegistration}.
 */
@RestController
@RequestMapping("/api")
public class EventRegistrationResource {

    private final Logger log = LoggerFactory.getLogger(EventRegistrationResource.class);

    private static final String ENTITY_NAME = "eventRegistration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventRegistrationService eventRegistrationService;

    private final EventRegistrationRepository eventRegistrationRepository;

    public EventRegistrationResource(
        EventRegistrationService eventRegistrationService,
        EventRegistrationRepository eventRegistrationRepository
    ) {
        this.eventRegistrationService = eventRegistrationService;
        this.eventRegistrationRepository = eventRegistrationRepository;
    }

    /**
     * {@code POST  /event-registrations} : Create a new eventRegistration.
     *
     * @param eventRegistrationDTO the eventRegistrationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventRegistrationDTO, or with status {@code 400 (Bad Request)} if the eventRegistration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-registrations")
    public ResponseEntity<EventRegistrationDTO> createEventRegistration(@Valid @RequestBody EventRegistrationDTO eventRegistrationDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventRegistration : {}", eventRegistrationDTO);
        if (eventRegistrationDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventRegistration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventRegistrationDTO result = eventRegistrationService.save(eventRegistrationDTO);
        return ResponseEntity.created(new URI("/api/event-registrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-registrations/:id} : Updates an existing eventRegistration.
     *
     * @param id the id of the eventRegistrationDTO to save.
     * @param eventRegistrationDTO the eventRegistrationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventRegistrationDTO,
     * or with status {@code 400 (Bad Request)} if the eventRegistrationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventRegistrationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-registrations/{id}")
    public ResponseEntity<EventRegistrationDTO> updateEventRegistration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventRegistrationDTO eventRegistrationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventRegistration : {}, {}", id, eventRegistrationDTO);
        if (eventRegistrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventRegistrationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventRegistrationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventRegistrationDTO result = eventRegistrationService.update(eventRegistrationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eventRegistrationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-registrations/:id} : Partial updates given fields of an existing eventRegistration, field will ignore if it is null
     *
     * @param id the id of the eventRegistrationDTO to save.
     * @param eventRegistrationDTO the eventRegistrationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventRegistrationDTO,
     * or with status {@code 400 (Bad Request)} if the eventRegistrationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventRegistrationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventRegistrationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/event-registrations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventRegistrationDTO> partialUpdateEventRegistration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventRegistrationDTO eventRegistrationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventRegistration partially : {}, {}", id, eventRegistrationDTO);
        if (eventRegistrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventRegistrationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventRegistrationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventRegistrationDTO> result = eventRegistrationService.partialUpdate(eventRegistrationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eventRegistrationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-registrations} : get all the eventRegistrations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventRegistrations in body.
     */
    @GetMapping("/event-registrations")
    public ResponseEntity<List<EventRegistrationDTO>> getAllEventRegistrations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EventRegistrations");
        Page<EventRegistrationDTO> page = eventRegistrationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-registrations/:id} : get the "id" eventRegistration.
     *
     * @param id the id of the eventRegistrationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventRegistrationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-registrations/{id}")
    public ResponseEntity<EventRegistrationDTO> getEventRegistration(@PathVariable Long id) {
        log.debug("REST request to get EventRegistration : {}", id);
        Optional<EventRegistrationDTO> eventRegistrationDTO = eventRegistrationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventRegistrationDTO);
    }

    /**
     * {@code DELETE  /event-registrations/:id} : delete the "id" eventRegistration.
     *
     * @param id the id of the eventRegistrationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-registrations/{id}")
    public ResponseEntity<Void> deleteEventRegistration(@PathVariable Long id) {
        log.debug("REST request to delete EventRegistration : {}", id);
        eventRegistrationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
