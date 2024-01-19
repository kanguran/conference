package com.conference.web.rest;

import com.conference.repository.EventContextRepository;
import com.conference.service.EventContextService;
import com.conference.service.dto.EventContextDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.conference.domain.EventContext}.
 */
@RestController
@RequestMapping("/api")
public class EventContextResource {

    private final Logger log = LoggerFactory.getLogger(EventContextResource.class);

    private static final String ENTITY_NAME = "eventContext";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventContextService eventContextService;

    private final EventContextRepository eventContextRepository;

    public EventContextResource(EventContextService eventContextService, EventContextRepository eventContextRepository) {
        this.eventContextService = eventContextService;
        this.eventContextRepository = eventContextRepository;
    }

    /**
     * {@code POST  /event-contexts} : Create a new eventContext.
     *
     * @param eventContextDTO the eventContextDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventContextDTO, or with status {@code 400 (Bad Request)} if the eventContext has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-contexts")
    public ResponseEntity<EventContextDTO> createEventContext(@Valid @RequestBody EventContextDTO eventContextDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventContext : {}", eventContextDTO);
        if (eventContextDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventContext cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventContextDTO result = eventContextService.save(eventContextDTO);
        return ResponseEntity
            .created(new URI("/api/event-contexts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-contexts/:id} : Updates an existing eventContext.
     *
     * @param id the id of the eventContextDTO to save.
     * @param eventContextDTO the eventContextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventContextDTO,
     * or with status {@code 400 (Bad Request)} if the eventContextDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventContextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-contexts/{id}")
    public ResponseEntity<EventContextDTO> updateEventContext(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventContextDTO eventContextDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventContext : {}, {}", id, eventContextDTO);
        if (eventContextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventContextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventContextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventContextDTO result = eventContextService.update(eventContextDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eventContextDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-contexts/:id} : Partial updates given fields of an existing eventContext, field will ignore if it is null
     *
     * @param id the id of the eventContextDTO to save.
     * @param eventContextDTO the eventContextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventContextDTO,
     * or with status {@code 400 (Bad Request)} if the eventContextDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventContextDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventContextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/event-contexts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventContextDTO> partialUpdateEventContext(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventContextDTO eventContextDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventContext partially : {}, {}", id, eventContextDTO);
        if (eventContextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventContextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventContextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventContextDTO> result = eventContextService.partialUpdate(eventContextDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eventContextDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-contexts} : get all the eventContexts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventContexts in body.
     */
    @GetMapping("/event-contexts")
    public ResponseEntity<List<EventContextDTO>> getAllEventContexts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of EventContexts");
        Page<EventContextDTO> page = eventContextService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-contexts/:id} : get the "id" eventContext.
     *
     * @param id the id of the eventContextDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventContextDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-contexts/{id}")
    public ResponseEntity<EventContextDTO> getEventContext(@PathVariable Long id) {
        log.debug("REST request to get EventContext : {}", id);
        Optional<EventContextDTO> eventContextDTO = eventContextService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventContextDTO);
    }

    /**
     * {@code DELETE  /event-contexts/:id} : delete the "id" eventContext.
     *
     * @param id the id of the eventContextDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-contexts/{id}")
    public ResponseEntity<Void> deleteEventContext(@PathVariable Long id) {
        log.debug("REST request to delete EventContext : {}", id);
        eventContextService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
