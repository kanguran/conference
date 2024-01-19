package com.conference.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.conference.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventContextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventContext.class);
        EventContext eventContext1 = new EventContext();
        eventContext1.setId(1L);
        EventContext eventContext2 = new EventContext();
        eventContext2.setId(eventContext1.getId());
        assertThat(eventContext1).isEqualTo(eventContext2);
        eventContext2.setId(2L);
        assertThat(eventContext1).isNotEqualTo(eventContext2);
        eventContext1.setId(null);
        assertThat(eventContext1).isNotEqualTo(eventContext2);
    }
}
