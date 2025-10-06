package com.conference.domain;

import static com.conference.domain.ApplicationUserTestSamples.*;
import static com.conference.domain.EventContextTestSamples.*;
import static com.conference.domain.EventRegistrationTestSamples.*;
import static com.conference.domain.EventTestSamples.*;
import static com.conference.domain.RoomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.conference.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EventContextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventContext.class);
        EventContext eventContext1 = getEventContextSample1();
        EventContext eventContext2 = new EventContext();
        assertThat(eventContext1).isNotEqualTo(eventContext2);

        eventContext2.setId(eventContext1.getId());
        assertThat(eventContext1).isEqualTo(eventContext2);

        eventContext2 = getEventContextSample2();
        assertThat(eventContext1).isNotEqualTo(eventContext2);
    }

    @Test
    void eventContextRoomTest() {
        EventContext eventContext = getEventContextRandomSampleGenerator();
        Room roomBack = getRoomRandomSampleGenerator();

        eventContext.setEventContextRoom(roomBack);
        assertThat(eventContext.getEventContextRoom()).isEqualTo(roomBack);

        eventContext.eventContextRoom(null);
        assertThat(eventContext.getEventContextRoom()).isNull();
    }

    @Test
    void contextHostTest() {
        EventContext eventContext = getEventContextRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        eventContext.setContextHost(applicationUserBack);
        assertThat(eventContext.getContextHost()).isEqualTo(applicationUserBack);

        eventContext.contextHost(null);
        assertThat(eventContext.getContextHost()).isNull();
    }

    @Test
    void eventContextRegistrationTest() {
        EventContext eventContext = getEventContextRandomSampleGenerator();
        EventRegistration eventRegistrationBack = getEventRegistrationRandomSampleGenerator();

        eventContext.addEventContextRegistration(eventRegistrationBack);
        assertThat(eventContext.getEventContextRegistrations()).containsOnly(eventRegistrationBack);
        assertThat(eventRegistrationBack.getEventContext()).isEqualTo(eventContext);

        eventContext.removeEventContextRegistration(eventRegistrationBack);
        assertThat(eventContext.getEventContextRegistrations()).doesNotContain(eventRegistrationBack);
        assertThat(eventRegistrationBack.getEventContext()).isNull();

        eventContext.eventContextRegistrations(new HashSet<>(Set.of(eventRegistrationBack)));
        assertThat(eventContext.getEventContextRegistrations()).containsOnly(eventRegistrationBack);
        assertThat(eventRegistrationBack.getEventContext()).isEqualTo(eventContext);

        eventContext.setEventContextRegistrations(new HashSet<>());
        assertThat(eventContext.getEventContextRegistrations()).doesNotContain(eventRegistrationBack);
        assertThat(eventRegistrationBack.getEventContext()).isNull();
    }

    @Test
    void eventTest() {
        EventContext eventContext = getEventContextRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        eventContext.setEvent(eventBack);
        assertThat(eventContext.getEvent()).isEqualTo(eventBack);

        eventContext.event(null);
        assertThat(eventContext.getEvent()).isNull();
    }
}
