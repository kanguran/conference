package com.conference.service.mapper;

import static com.conference.domain.EventRegistrationAsserts.*;
import static com.conference.domain.EventRegistrationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventRegistrationMapperTest {

    private EventRegistrationMapper eventRegistrationMapper;

    @BeforeEach
    void setUp() {
        eventRegistrationMapper = new EventRegistrationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEventRegistrationSample1();
        var actual = eventRegistrationMapper.toEntity(eventRegistrationMapper.toDto(expected));
        assertEventRegistrationAllPropertiesEquals(expected, actual);
    }
}
