package com.conference.web.rest;

import static com.conference.domain.EventContextAsserts.*;
import static com.conference.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.conference.IntegrationTest;
import com.conference.domain.EventContext;
import com.conference.domain.enumeration.EventContextStatus;
import com.conference.repository.EventContextRepository;
import com.conference.service.dto.EventContextDTO;
import com.conference.service.mapper.EventContextMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link EventContextResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventContextResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final EventContextStatus DEFAULT_EVENT_CONTEXT_STATUS = EventContextStatus.AVAILABLE;
    private static final EventContextStatus UPDATED_EVENT_CONTEXT_STATUS = EventContextStatus.FULLY_BOOKED;

    private static final Instant DEFAULT_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/event-contexts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EventContextRepository eventContextRepository;

    @Autowired
    private EventContextMapper eventContextMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventContextMockMvc;

    private EventContext eventContext;

    private EventContext insertedEventContext;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventContext createEntity() {
        return new EventContext()
            .description(DEFAULT_DESCRIPTION)
            .eventContextStatus(DEFAULT_EVENT_CONTEXT_STATUS)
            .start(DEFAULT_START)
            .end(DEFAULT_END);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventContext createUpdatedEntity() {
        return new EventContext()
            .description(UPDATED_DESCRIPTION)
            .eventContextStatus(UPDATED_EVENT_CONTEXT_STATUS)
            .start(UPDATED_START)
            .end(UPDATED_END);
    }

    @BeforeEach
    void initTest() {
        eventContext = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEventContext != null) {
            eventContextRepository.delete(insertedEventContext);
            insertedEventContext = null;
        }
    }

    @Test
    @Transactional
    void createEventContext() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EventContext
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);
        var returnedEventContextDTO = om.readValue(
            restEventContextMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventContextDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EventContextDTO.class
        );

        // Validate the EventContext in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEventContext = eventContextMapper.toEntity(returnedEventContextDTO);
        assertEventContextUpdatableFieldsEquals(returnedEventContext, getPersistedEventContext(returnedEventContext));

        insertedEventContext = returnedEventContext;
    }

    @Test
    @Transactional
    void createEventContextWithExistingId() throws Exception {
        // Create the EventContext with an existing ID
        eventContext.setId(1L);
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventContextDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventContext in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eventContext.setDescription(null);

        // Create the EventContext, which fails.
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        restEventContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventContextDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventContextStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eventContext.setEventContextStatus(null);

        // Create the EventContext, which fails.
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        restEventContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventContextDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eventContext.setStart(null);

        // Create the EventContext, which fails.
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        restEventContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventContextDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eventContext.setEnd(null);

        // Create the EventContext, which fails.
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        restEventContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventContextDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventContexts() throws Exception {
        // Initialize the database
        insertedEventContext = eventContextRepository.saveAndFlush(eventContext);

        // Get all the eventContextList
        restEventContextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventContext.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].eventContextStatus").value(hasItem(DEFAULT_EVENT_CONTEXT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())));
    }

    @Test
    @Transactional
    void getEventContext() throws Exception {
        // Initialize the database
        insertedEventContext = eventContextRepository.saveAndFlush(eventContext);

        // Get the eventContext
        restEventContextMockMvc
            .perform(get(ENTITY_API_URL_ID, eventContext.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventContext.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.eventContextStatus").value(DEFAULT_EVENT_CONTEXT_STATUS.toString()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START.toString()))
            .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEventContext() throws Exception {
        // Get the eventContext
        restEventContextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventContext() throws Exception {
        // Initialize the database
        insertedEventContext = eventContextRepository.saveAndFlush(eventContext);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventContext
        EventContext updatedEventContext = eventContextRepository.findById(eventContext.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventContext are not directly saved in db
        em.detach(updatedEventContext);
        updatedEventContext
            .description(UPDATED_DESCRIPTION)
            .eventContextStatus(UPDATED_EVENT_CONTEXT_STATUS)
            .start(UPDATED_START)
            .end(UPDATED_END);
        EventContextDTO eventContextDTO = eventContextMapper.toDto(updatedEventContext);

        restEventContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventContextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventContextDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventContext in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEventContextToMatchAllProperties(updatedEventContext);
    }

    @Test
    @Transactional
    void putNonExistingEventContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventContext.setId(longCount.incrementAndGet());

        // Create the EventContext
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventContextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventContextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventContext in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventContext.setId(longCount.incrementAndGet());

        // Create the EventContext
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventContextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventContext in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventContext.setId(longCount.incrementAndGet());

        // Create the EventContext
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventContextMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventContextDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventContext in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventContextWithPatch() throws Exception {
        // Initialize the database
        insertedEventContext = eventContextRepository.saveAndFlush(eventContext);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventContext using partial update
        EventContext partialUpdatedEventContext = new EventContext();
        partialUpdatedEventContext.setId(eventContext.getId());

        partialUpdatedEventContext.start(UPDATED_START).end(UPDATED_END);

        restEventContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventContext.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEventContext))
            )
            .andExpect(status().isOk());

        // Validate the EventContext in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventContextUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEventContext, eventContext),
            getPersistedEventContext(eventContext)
        );
    }

    @Test
    @Transactional
    void fullUpdateEventContextWithPatch() throws Exception {
        // Initialize the database
        insertedEventContext = eventContextRepository.saveAndFlush(eventContext);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventContext using partial update
        EventContext partialUpdatedEventContext = new EventContext();
        partialUpdatedEventContext.setId(eventContext.getId());

        partialUpdatedEventContext
            .description(UPDATED_DESCRIPTION)
            .eventContextStatus(UPDATED_EVENT_CONTEXT_STATUS)
            .start(UPDATED_START)
            .end(UPDATED_END);

        restEventContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventContext.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEventContext))
            )
            .andExpect(status().isOk());

        // Validate the EventContext in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventContextUpdatableFieldsEquals(partialUpdatedEventContext, getPersistedEventContext(partialUpdatedEventContext));
    }

    @Test
    @Transactional
    void patchNonExistingEventContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventContext.setId(longCount.incrementAndGet());

        // Create the EventContext
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventContextDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventContextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventContext in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventContext.setId(longCount.incrementAndGet());

        // Create the EventContext
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventContextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventContext in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventContext.setId(longCount.incrementAndGet());

        // Create the EventContext
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventContextMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(eventContextDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventContext in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventContext() throws Exception {
        // Initialize the database
        insertedEventContext = eventContextRepository.saveAndFlush(eventContext);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the eventContext
        restEventContextMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventContext.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return eventContextRepository.count();
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

    protected EventContext getPersistedEventContext(EventContext eventContext) {
        return eventContextRepository.findById(eventContext.getId()).orElseThrow();
    }

    protected void assertPersistedEventContextToMatchAllProperties(EventContext expectedEventContext) {
        assertEventContextAllPropertiesEquals(expectedEventContext, getPersistedEventContext(expectedEventContext));
    }

    protected void assertPersistedEventContextToMatchUpdatableProperties(EventContext expectedEventContext) {
        assertEventContextAllUpdatablePropertiesEquals(expectedEventContext, getPersistedEventContext(expectedEventContext));
    }
}
