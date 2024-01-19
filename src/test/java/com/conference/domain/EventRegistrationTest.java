package com.conference.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.conference.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventRegistrationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventRegistration.class);
        EventRegistration eventRegistration1 = new EventRegistration();
        eventRegistration1.setId(1L);
        EventRegistration eventRegistration2 = new EventRegistration();
        eventRegistration2.setId(eventRegistration1.getId());
        assertThat(eventRegistration1).isEqualTo(eventRegistration2);
        eventRegistration2.setId(2L);
        assertThat(eventRegistration1).isNotEqualTo(eventRegistration2);
        eventRegistration1.setId(null);
        assertThat(eventRegistration1).isNotEqualTo(eventRegistration2);
    }
}
