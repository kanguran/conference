package com.conference.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventContextMapperTest {

    private EventContextMapper eventContextMapper;

    @BeforeEach
    public void setUp() {
        eventContextMapper = new EventContextMapperImpl();
    }
}
