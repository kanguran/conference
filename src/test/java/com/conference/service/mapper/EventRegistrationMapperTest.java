package com.conference.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventRegistrationMapperTest {

    private EventRegistrationMapper eventRegistrationMapper;

    @BeforeEach
    public void setUp() {
        eventRegistrationMapper = new EventRegistrationMapperImpl();
    }
}
