package com.conference.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.conference.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventRegistrationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventRegistrationDTO.class);
        EventRegistrationDTO eventRegistrationDTO1 = new EventRegistrationDTO();
        eventRegistrationDTO1.setId(1L);
        EventRegistrationDTO eventRegistrationDTO2 = new EventRegistrationDTO();
        assertThat(eventRegistrationDTO1).isNotEqualTo(eventRegistrationDTO2);
        eventRegistrationDTO2.setId(eventRegistrationDTO1.getId());
        assertThat(eventRegistrationDTO1).isEqualTo(eventRegistrationDTO2);
        eventRegistrationDTO2.setId(2L);
        assertThat(eventRegistrationDTO1).isNotEqualTo(eventRegistrationDTO2);
        eventRegistrationDTO1.setId(null);
        assertThat(eventRegistrationDTO1).isNotEqualTo(eventRegistrationDTO2);
    }
}
