package com.conference.service.mapper;


import org.junit.jupiter.api.BeforeEach;

class EventContextMapperTest {

    private EventContextMapper eventContextMapper;

    @BeforeEach
    public void setUp() {
        eventContextMapper = new EventContextMapperImpl();
    }
}
