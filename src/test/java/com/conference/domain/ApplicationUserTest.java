package com.conference.domain;

import static com.conference.domain.ApplicationUserTestSamples.*;
import static com.conference.domain.EventContextTestSamples.*;
import static com.conference.domain.EventRegistrationTestSamples.*;
import static com.conference.domain.EventTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.conference.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUser.class);
        ApplicationUser applicationUser1 = getApplicationUserSample1();
        ApplicationUser applicationUser2 = new ApplicationUser();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);

        applicationUser2.setId(applicationUser1.getId());
        assertThat(applicationUser1).isEqualTo(applicationUser2);

        applicationUser2 = getApplicationUserSample2();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);
    }

    @Test
    void eventTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        applicationUser.setEvent(eventBack);
        assertThat(applicationUser.getEvent()).isEqualTo(eventBack);
        assertThat(eventBack.getMainHost()).isEqualTo(applicationUser);

        applicationUser.event(null);
        assertThat(applicationUser.getEvent()).isNull();
        assertThat(eventBack.getMainHost()).isNull();
    }

    @Test
    void eventContextTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        EventContext eventContextBack = getEventContextRandomSampleGenerator();

        applicationUser.setEventContext(eventContextBack);
        assertThat(applicationUser.getEventContext()).isEqualTo(eventContextBack);
        assertThat(eventContextBack.getContextHost()).isEqualTo(applicationUser);

        applicationUser.eventContext(null);
        assertThat(applicationUser.getEventContext()).isNull();
        assertThat(eventContextBack.getContextHost()).isNull();
    }

    @Test
    void eventRegistrationTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        EventRegistration eventRegistrationBack = getEventRegistrationRandomSampleGenerator();

        applicationUser.setEventRegistration(eventRegistrationBack);
        assertThat(applicationUser.getEventRegistration()).isEqualTo(eventRegistrationBack);
        assertThat(eventRegistrationBack.getEventCounterparty()).isEqualTo(applicationUser);

        applicationUser.eventRegistration(null);
        assertThat(applicationUser.getEventRegistration()).isNull();
        assertThat(eventRegistrationBack.getEventCounterparty()).isNull();
    }
}
