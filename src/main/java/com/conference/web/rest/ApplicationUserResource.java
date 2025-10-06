package com.conference.web.rest;

import com.conference.repository.ApplicationUserRepository;
import com.conference.service.ApplicationUserService;
import com.conference.service.dto.ApplicationUserDTO;
import com.conference.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.conference.domain.ApplicationUser}.
 */
@RestController
@RequestMapping("/api/application-users")
public class ApplicationUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationUserResource.class);

    private static final String ENTITY_NAME = "applicationUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationUserService applicationUserService;

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserResource(ApplicationUserService applicationUserService, ApplicationUserRepository applicationUserRepository) {
        this.applicationUserService = applicationUserService;
        this.applicationUserRepository = applicationUserRepository;
    }

    /**
     * {@code POST  /application-users} : Create a new applicationUser.
     *
     * @param applicationUserDTO the applicationUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationUserDTO, or with status {@code 400 (Bad Request)} if the applicationUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApplicationUserDTO> createApplicationUser(@RequestBody ApplicationUserDTO applicationUserDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ApplicationUser : {}", applicationUserDTO);
        if (applicationUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new applicationUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        applicationUserDTO = applicationUserService.save(applicationUserDTO);
        return ResponseEntity.created(new URI("/api/application-users/" + applicationUserDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, applicationUserDTO.getId().toString()))
            .body(applicationUserDTO);
    }

    /**
     * {@code PUT  /application-users/:id} : Updates an existing applicationUser.
     *
     * @param id the id of the applicationUserDTO to save.
     * @param applicationUserDTO the applicationUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationUserDTO,
     * or with status {@code 400 (Bad Request)} if the applicationUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationUserDTO> updateApplicationUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApplicationUserDTO applicationUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ApplicationUser : {}, {}", id, applicationUserDTO);
        if (applicationUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        applicationUserDTO = applicationUserService.update(applicationUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicationUserDTO.getId().toString()))
            .body(applicationUserDTO);
    }

    /**
     * {@code PATCH  /application-users/:id} : Partial updates given fields of an existing applicationUser, field will ignore if it is null
     *
     * @param id the id of the applicationUserDTO to save.
     * @param applicationUserDTO the applicationUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationUserDTO,
     * or with status {@code 400 (Bad Request)} if the applicationUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the applicationUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApplicationUserDTO> partialUpdateApplicationUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApplicationUserDTO applicationUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ApplicationUser partially : {}, {}", id, applicationUserDTO);
        if (applicationUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApplicationUserDTO> result = applicationUserService.partialUpdate(applicationUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicationUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /application-users} : get all the applicationUsers.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationUsers in body.
     */
    @GetMapping("")
    public List<ApplicationUserDTO> getAllApplicationUsers(@RequestParam(name = "filter", required = false) String filter) {
        if ("event-is-null".equals(filter)) {
            LOG.debug("REST request to get all ApplicationUsers where event is null");
            return applicationUserService.findAllWhereEventIsNull();
        }

        if ("eventcontext-is-null".equals(filter)) {
            LOG.debug("REST request to get all ApplicationUsers where eventContext is null");
            return applicationUserService.findAllWhereEventContextIsNull();
        }

        if ("eventregistration-is-null".equals(filter)) {
            LOG.debug("REST request to get all ApplicationUsers where eventRegistration is null");
            return applicationUserService.findAllWhereEventRegistrationIsNull();
        }
        LOG.debug("REST request to get all ApplicationUsers");
        return applicationUserService.findAll();
    }

    /**
     * {@code GET  /application-users/:id} : get the "id" applicationUser.
     *
     * @param id the id of the applicationUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationUserDTO> getApplicationUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ApplicationUser : {}", id);
        Optional<ApplicationUserDTO> applicationUserDTO = applicationUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applicationUserDTO);
    }

    /**
     * {@code DELETE  /application-users/:id} : delete the "id" applicationUser.
     *
     * @param id the id of the applicationUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplicationUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ApplicationUser : {}", id);
        applicationUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
