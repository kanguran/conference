package com.conference.web.rest;

import static com.conference.domain.EventRegistrationAsserts.*;
import static com.conference.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.conference.IntegrationTest;
import com.conference.domain.EventRegistration;
import com.conference.domain.enumeration.EventRegistrationStatus;
import com.conference.repository.EventRegistrationRepository;
import com.conference.security.AuthoritiesConstants;
import com.conference.service.dto.EventRegistrationDTO;
import com.conference.service.mapper.EventRegistrationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EventRegistrationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventRegistrationResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final EventRegistrationStatus DEFAULT_EVENT_REGISTRATION_STATUS = EventRegistrationStatus.ACTIVE;
    private static final EventRegistrationStatus UPDATED_EVENT_REGISTRATION_STATUS = EventRegistrationStatus.CANCELLED;

    private static final String ENTITY_API_URL = "/api/event-registrations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    @Autowired
    private EventRegistrationMapper eventRegistrationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventRegistrationMockMvc;

    private EventRegistration eventRegistration;

    private EventRegistration insertedEventRegistration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventRegistration createEntity() {
        return new EventRegistration().description(DEFAULT_DESCRIPTION).eventRegistrationStatus(DEFAULT_EVENT_REGISTRATION_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventRegistration createUpdatedEntity() {
        return new EventRegistration().description(UPDATED_DESCRIPTION).eventRegistrationStatus(UPDATED_EVENT_REGISTRATION_STATUS);
    }

    @BeforeEach
    void initTest() {
        eventRegistration = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEventRegistration != null) {
            eventRegistrationRepository.delete(insertedEventRegistration);
            insertedEventRegistration = null;
        }
    }

    @Test
    @Transactional
    void createEventRegistration() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EventRegistration
        EventRegistrationDTO eventRegistrationDTO = eventRegistrationMapper.toDto(eventRegistration);
        var returnedEventRegistrationDTO = om.readValue(
            restEventRegistrationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventRegistrationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EventRegistrationDTO.class
        );

        // Validate the EventRegistration in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEventRegistration = eventRegistrationMapper.toEntity(returnedEventRegistrationDTO);
        assertEventRegistrationUpdatableFieldsEquals(returnedEventRegistration, getPersistedEventRegistration(returnedEventRegistration));

        insertedEventRegistration = returnedEventRegistration;
    }

    @Test
    @Transactional
    void createEventRegistrationWithExistingId() throws Exception {
        // Create the EventRegistration with an existing ID
        eventRegistration.setId(1L);
        EventRegistrationDTO eventRegistrationDTO = eventRegistrationMapper.toDto(eventRegistration);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventRegistrationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventRegistrationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventRegistrationStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eventRegistration.setEventRegistrationStatus(null);

        // Create the EventRegistration, which fails.
        EventRegistrationDTO eventRegistrationDTO = eventRegistrationMapper.toDto(eventRegistration);

        restEventRegistrationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventRegistrationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    @Transactional
    void getAllEventRegistrations() throws Exception {
        // Initialize the database
        insertedEventRegistration = eventRegistrationRepository.saveAndFlush(eventRegistration);

        // Get all the eventRegistrationList
        restEventRegistrationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventRegistration.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].eventRegistrationStatus").value(hasItem(DEFAULT_EVENT_REGISTRATION_STATUS.toString())));
    }

    @Test
    @Transactional
    void getEventRegistration() throws Exception {
        // Initialize the database
        insertedEventRegistration = eventRegistrationRepository.saveAndFlush(eventRegistration);

        // Get the eventRegistration
        restEventRegistrationMockMvc
            .perform(get(ENTITY_API_URL_ID, eventRegistration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventRegistration.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.eventRegistrationStatus").value(DEFAULT_EVENT_REGISTRATION_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEventRegistration() throws Exception {
        // Get the eventRegistration
        restEventRegistrationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventRegistration() throws Exception {
        // Initialize the database
        insertedEventRegistration = eventRegistrationRepository.saveAndFlush(eventRegistration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventRegistration
        EventRegistration updatedEventRegistration = eventRegistrationRepository.findById(eventRegistration.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventRegistration are not directly saved in db
        em.detach(updatedEventRegistration);
        updatedEventRegistration.description(UPDATED_DESCRIPTION).eventRegistrationStatus(UPDATED_EVENT_REGISTRATION_STATUS);
        EventRegistrationDTO eventRegistrationDTO = eventRegistrationMapper.toDto(updatedEventRegistration);

        restEventRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventRegistrationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventRegistrationDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEventRegistrationToMatchAllProperties(updatedEventRegistration);
    }

    @Test
    @Transactional
    void putNonExistingEventRegistration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventRegistration.setId(longCount.incrementAndGet());

        // Create the EventRegistration
        EventRegistrationDTO eventRegistrationDTO = eventRegistrationMapper.toDto(eventRegistration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventRegistrationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventRegistration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventRegistration.setId(longCount.incrementAndGet());

        // Create the EventRegistration
        EventRegistrationDTO eventRegistrationDTO = eventRegistrationMapper.toDto(eventRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventRegistration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventRegistration.setId(longCount.incrementAndGet());

        // Create the EventRegistration
        EventRegistrationDTO eventRegistrationDTO = eventRegistrationMapper.toDto(eventRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventRegistrationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventRegistrationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventRegistrationWithPatch() throws Exception {
        // Initialize the database
        insertedEventRegistration = eventRegistrationRepository.saveAndFlush(eventRegistration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventRegistration using partial update
        EventRegistration partialUpdatedEventRegistration = new EventRegistration();
        partialUpdatedEventRegistration.setId(eventRegistration.getId());

        restEventRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventRegistration.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEventRegistration))
            )
            .andExpect(status().isOk());

        // Validate the EventRegistration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventRegistrationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEventRegistration, eventRegistration),
            getPersistedEventRegistration(eventRegistration)
        );
    }

    @Test
    @Transactional
    void fullUpdateEventRegistrationWithPatch() throws Exception {
        // Initialize the database
        insertedEventRegistration = eventRegistrationRepository.saveAndFlush(eventRegistration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventRegistration using partial update
        EventRegistration partialUpdatedEventRegistration = new EventRegistration();
        partialUpdatedEventRegistration.setId(eventRegistration.getId());

        partialUpdatedEventRegistration.description(UPDATED_DESCRIPTION).eventRegistrationStatus(UPDATED_EVENT_REGISTRATION_STATUS);

        restEventRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventRegistration.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEventRegistration))
            )
            .andExpect(status().isOk());

        // Validate the EventRegistration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventRegistrationUpdatableFieldsEquals(
            partialUpdatedEventRegistration,
            getPersistedEventRegistration(partialUpdatedEventRegistration)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEventRegistration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventRegistration.setId(longCount.incrementAndGet());

        // Create the EventRegistration
        EventRegistrationDTO eventRegistrationDTO = eventRegistrationMapper.toDto(eventRegistration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventRegistrationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventRegistration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventRegistration.setId(longCount.incrementAndGet());

        // Create the EventRegistration
        EventRegistrationDTO eventRegistrationDTO = eventRegistrationMapper.toDto(eventRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventRegistration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventRegistration.setId(longCount.incrementAndGet());

        // Create the EventRegistration
        EventRegistrationDTO eventRegistrationDTO = eventRegistrationMapper.toDto(eventRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventRegistrationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(eventRegistrationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventRegistration() throws Exception {
        // Initialize the database
        insertedEventRegistration = eventRegistrationRepository.saveAndFlush(eventRegistration);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the eventRegistration
        restEventRegistrationMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventRegistration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return eventRegistrationRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected EventRegistration getPersistedEventRegistration(EventRegistration eventRegistration) {
        return eventRegistrationRepository.findById(eventRegistration.getId()).orElseThrow();
    }

    protected void assertPersistedEventRegistrationToMatchAllProperties(EventRegistration expectedEventRegistration) {
        assertEventRegistrationAllPropertiesEquals(expectedEventRegistration, getPersistedEventRegistration(expectedEventRegistration));
    }

    protected void assertPersistedEventRegistrationToMatchUpdatableProperties(EventRegistration expectedEventRegistration) {
        assertEventRegistrationAllUpdatablePropertiesEquals(
            expectedEventRegistration,
            getPersistedEventRegistration(expectedEventRegistration)
        );
    }
}
