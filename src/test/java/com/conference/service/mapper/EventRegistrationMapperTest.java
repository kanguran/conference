package com.conference.service.mapper;


import org.junit.jupiter.api.BeforeEach;

class EventRegistrationMapperTest {

    private EventRegistrationMapper eventRegistrationMapper;

    @BeforeEach
    public void setUp() {
        eventRegistrationMapper = new EventRegistrationMapperImpl();
    }
}
