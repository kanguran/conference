package com.conference.domain;

import static com.conference.domain.ApplicationUserTestSamples.*;
import static com.conference.domain.EventContextTestSamples.*;
import static com.conference.domain.EventTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.conference.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Event.class);
        Event event1 = getEventSample1();
        Event event2 = new Event();
        assertThat(event1).isNotEqualTo(event2);

        event2.setId(event1.getId());
        assertThat(event1).isEqualTo(event2);

        event2 = getEventSample2();
        assertThat(event1).isNotEqualTo(event2);
    }

    @Test
    void eventContextTest() {
        Event event = getEventRandomSampleGenerator();
        EventContext eventContextBack = getEventContextRandomSampleGenerator();

        event.addEventContext(eventContextBack);
        assertThat(event.getEventContexts()).containsOnly(eventContextBack);
        assertThat(eventContextBack.getEvent()).isEqualTo(event);

        event.removeEventContext(eventContextBack);
        assertThat(event.getEventContexts()).doesNotContain(eventContextBack);
        assertThat(eventContextBack.getEvent()).isNull();

        event.eventContexts(new HashSet<>(Set.of(eventContextBack)));
        assertThat(event.getEventContexts()).containsOnly(eventContextBack);
        assertThat(eventContextBack.getEvent()).isEqualTo(event);

        event.setEventContexts(new HashSet<>());
        assertThat(event.getEventContexts()).doesNotContain(eventContextBack);
        assertThat(eventContextBack.getEvent()).isNull();
    }

    @Test
    void mainHostTest() {
        Event event = getEventRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        event.setMainHost(applicationUserBack);
        assertThat(event.getMainHost()).isEqualTo(applicationUserBack);

        event.mainHost(null);
        assertThat(event.getMainHost()).isNull();
    }
}
