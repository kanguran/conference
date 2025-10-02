package com.conference.domain;

import static com.conference.domain.ApplicationUserTestSamples.*;
import static com.conference.domain.EventContextTestSamples.*;
import static com.conference.domain.EventRegistrationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.conference.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventRegistrationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventRegistration.class);
        EventRegistration eventRegistration1 = getEventRegistrationSample1();
        EventRegistration eventRegistration2 = new EventRegistration();
        assertThat(eventRegistration1).isNotEqualTo(eventRegistration2);

        eventRegistration2.setId(eventRegistration1.getId());
        assertThat(eventRegistration1).isEqualTo(eventRegistration2);

        eventRegistration2 = getEventRegistrationSample2();
        assertThat(eventRegistration1).isNotEqualTo(eventRegistration2);
    }

    @Test
    void eventCounterpartyTest() {
        EventRegistration eventRegistration = getEventRegistrationRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        eventRegistration.setEventCounterparty(applicationUserBack);
        assertThat(eventRegistration.getEventCounterparty()).isEqualTo(applicationUserBack);

        eventRegistration.eventCounterparty(null);
        assertThat(eventRegistration.getEventCounterparty()).isNull();
    }

    @Test
    void eventContextTest() {
        EventRegistration eventRegistration = getEventRegistrationRandomSampleGenerator();
        EventContext eventContextBack = getEventContextRandomSampleGenerator();

        eventRegistration.setEventContext(eventContextBack);
        assertThat(eventRegistration.getEventContext()).isEqualTo(eventContextBack);

        eventRegistration.eventContext(null);
        assertThat(eventRegistration.getEventContext()).isNull();
    }
}
