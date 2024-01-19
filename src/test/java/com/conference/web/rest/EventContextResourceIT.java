package com.conference.web.rest;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
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

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final EventContextStatus DEFAULT_EVENT_CONTEXT_STATUS = EventContextStatus.AVAILABLE;
    private static final EventContextStatus UPDATED_EVENT_CONTEXT_STATUS = EventContextStatus.FULLY_BOOKED;

    private static final LocalDate DEFAULT_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/event-contexts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventContextRepository eventContextRepository;

    @Autowired
    private EventContextMapper eventContextMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventContextMockMvc;

    private EventContext eventContext;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventContext createEntity(EntityManager em) {
        EventContext eventContext = new EventContext()
            .name(DEFAULT_NAME)
            .eventContextStatus(DEFAULT_EVENT_CONTEXT_STATUS)
            .start(DEFAULT_START)
            .end(DEFAULT_END);
        return eventContext;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventContext createUpdatedEntity(EntityManager em) {
        EventContext eventContext = new EventContext()
            .name(UPDATED_NAME)
            .eventContextStatus(UPDATED_EVENT_CONTEXT_STATUS)
            .start(UPDATED_START)
            .end(UPDATED_END);
        return eventContext;
    }

    @BeforeEach
    public void initTest() {
        eventContext = createEntity(em);
    }

    @Test
    @Transactional
    void createEventContext() throws Exception {
        int databaseSizeBeforeCreate = eventContextRepository.findAll().size();
        // Create the EventContext
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);
        restEventContextMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventContextDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventContext in the database
        List<EventContext> eventContextList = eventContextRepository.findAll();
        assertThat(eventContextList).hasSize(databaseSizeBeforeCreate + 1);
        EventContext testEventContext = eventContextList.get(eventContextList.size() - 1);
        assertThat(testEventContext.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventContext.getEventContextStatus()).isEqualTo(DEFAULT_EVENT_CONTEXT_STATUS);
        assertThat(testEventContext.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testEventContext.getEnd()).isEqualTo(DEFAULT_END);
    }

    @Test
    @Transactional
    void createEventContextWithExistingId() throws Exception {
        // Create the EventContext with an existing ID
        eventContext.setId(1L);
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        int databaseSizeBeforeCreate = eventContextRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventContextMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventContextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventContext in the database
        List<EventContext> eventContextList = eventContextRepository.findAll();
        assertThat(eventContextList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventContextRepository.findAll().size();
        // set the field null
        eventContext.setName(null);

        // Create the EventContext, which fails.
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        restEventContextMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventContextDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventContext> eventContextList = eventContextRepository.findAll();
        assertThat(eventContextList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventContexts() throws Exception {
        // Initialize the database
        eventContextRepository.saveAndFlush(eventContext);

        // Get all the eventContextList
        restEventContextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventContext.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].eventContextStatus").value(hasItem(DEFAULT_EVENT_CONTEXT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())));
    }

    @Test
    @Transactional
    void getEventContext() throws Exception {
        // Initialize the database
        eventContextRepository.saveAndFlush(eventContext);

        // Get the eventContext
        restEventContextMockMvc
            .perform(get(ENTITY_API_URL_ID, eventContext.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventContext.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
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
        eventContextRepository.saveAndFlush(eventContext);

        int databaseSizeBeforeUpdate = eventContextRepository.findAll().size();

        // Update the eventContext
        EventContext updatedEventContext = eventContextRepository.findById(eventContext.getId()).get();
        // Disconnect from session so that the updates on updatedEventContext are not directly saved in db
        em.detach(updatedEventContext);
        updatedEventContext.name(UPDATED_NAME).eventContextStatus(UPDATED_EVENT_CONTEXT_STATUS).start(UPDATED_START).end(UPDATED_END);
        EventContextDTO eventContextDTO = eventContextMapper.toDto(updatedEventContext);

        restEventContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventContextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventContextDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventContext in the database
        List<EventContext> eventContextList = eventContextRepository.findAll();
        assertThat(eventContextList).hasSize(databaseSizeBeforeUpdate);
        EventContext testEventContext = eventContextList.get(eventContextList.size() - 1);
        assertThat(testEventContext.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventContext.getEventContextStatus()).isEqualTo(UPDATED_EVENT_CONTEXT_STATUS);
        assertThat(testEventContext.getStart()).isEqualTo(UPDATED_START);
        assertThat(testEventContext.getEnd()).isEqualTo(UPDATED_END);
    }

    @Test
    @Transactional
    void putNonExistingEventContext() throws Exception {
        int databaseSizeBeforeUpdate = eventContextRepository.findAll().size();
        eventContext.setId(count.incrementAndGet());

        // Create the EventContext
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventContextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventContextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventContext in the database
        List<EventContext> eventContextList = eventContextRepository.findAll();
        assertThat(eventContextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventContext() throws Exception {
        int databaseSizeBeforeUpdate = eventContextRepository.findAll().size();
        eventContext.setId(count.incrementAndGet());

        // Create the EventContext
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventContextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventContext in the database
        List<EventContext> eventContextList = eventContextRepository.findAll();
        assertThat(eventContextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventContext() throws Exception {
        int databaseSizeBeforeUpdate = eventContextRepository.findAll().size();
        eventContext.setId(count.incrementAndGet());

        // Create the EventContext
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventContextMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventContextDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventContext in the database
        List<EventContext> eventContextList = eventContextRepository.findAll();
        assertThat(eventContextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventContextWithPatch() throws Exception {
        // Initialize the database
        eventContextRepository.saveAndFlush(eventContext);

        int databaseSizeBeforeUpdate = eventContextRepository.findAll().size();

        // Update the eventContext using partial update
        EventContext partialUpdatedEventContext = new EventContext();
        partialUpdatedEventContext.setId(eventContext.getId());

        partialUpdatedEventContext.name(UPDATED_NAME);

        restEventContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventContext.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventContext))
            )
            .andExpect(status().isOk());

        // Validate the EventContext in the database
        List<EventContext> eventContextList = eventContextRepository.findAll();
        assertThat(eventContextList).hasSize(databaseSizeBeforeUpdate);
        EventContext testEventContext = eventContextList.get(eventContextList.size() - 1);
        assertThat(testEventContext.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventContext.getEventContextStatus()).isEqualTo(DEFAULT_EVENT_CONTEXT_STATUS);
        assertThat(testEventContext.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testEventContext.getEnd()).isEqualTo(DEFAULT_END);
    }

    @Test
    @Transactional
    void fullUpdateEventContextWithPatch() throws Exception {
        // Initialize the database
        eventContextRepository.saveAndFlush(eventContext);

        int databaseSizeBeforeUpdate = eventContextRepository.findAll().size();

        // Update the eventContext using partial update
        EventContext partialUpdatedEventContext = new EventContext();
        partialUpdatedEventContext.setId(eventContext.getId());

        partialUpdatedEventContext
            .name(UPDATED_NAME)
            .eventContextStatus(UPDATED_EVENT_CONTEXT_STATUS)
            .start(UPDATED_START)
            .end(UPDATED_END);

        restEventContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventContext.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventContext))
            )
            .andExpect(status().isOk());

        // Validate the EventContext in the database
        List<EventContext> eventContextList = eventContextRepository.findAll();
        assertThat(eventContextList).hasSize(databaseSizeBeforeUpdate);
        EventContext testEventContext = eventContextList.get(eventContextList.size() - 1);
        assertThat(testEventContext.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventContext.getEventContextStatus()).isEqualTo(UPDATED_EVENT_CONTEXT_STATUS);
        assertThat(testEventContext.getStart()).isEqualTo(UPDATED_START);
        assertThat(testEventContext.getEnd()).isEqualTo(UPDATED_END);
    }

    @Test
    @Transactional
    void patchNonExistingEventContext() throws Exception {
        int databaseSizeBeforeUpdate = eventContextRepository.findAll().size();
        eventContext.setId(count.incrementAndGet());

        // Create the EventContext
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventContextDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventContextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventContext in the database
        List<EventContext> eventContextList = eventContextRepository.findAll();
        assertThat(eventContextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventContext() throws Exception {
        int databaseSizeBeforeUpdate = eventContextRepository.findAll().size();
        eventContext.setId(count.incrementAndGet());

        // Create the EventContext
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventContextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventContext in the database
        List<EventContext> eventContextList = eventContextRepository.findAll();
        assertThat(eventContextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventContext() throws Exception {
        int databaseSizeBeforeUpdate = eventContextRepository.findAll().size();
        eventContext.setId(count.incrementAndGet());

        // Create the EventContext
        EventContextDTO eventContextDTO = eventContextMapper.toDto(eventContext);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventContextMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventContextDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventContext in the database
        List<EventContext> eventContextList = eventContextRepository.findAll();
        assertThat(eventContextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventContext() throws Exception {
        // Initialize the database
        eventContextRepository.saveAndFlush(eventContext);

        int databaseSizeBeforeDelete = eventContextRepository.findAll().size();

        // Delete the eventContext
        restEventContextMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventContext.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventContext> eventContextList = eventContextRepository.findAll();
        assertThat(eventContextList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
