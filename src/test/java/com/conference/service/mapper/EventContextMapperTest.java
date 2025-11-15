package com.conference.service.mapper;

import static com.conference.domain.EventContextAsserts.*;
import static com.conference.domain.EventContextTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventContextMapperTest {

    private EventContextMapper eventContextMapper;

    @BeforeEach
    void setUp() {
        eventContextMapper = new EventContextMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEventContextSample1();
        var actual = eventContextMapper.toEntity(eventContextMapper.toDto(expected));
        assertEventContextAllPropertiesEquals(expected, actual);
    }
}
