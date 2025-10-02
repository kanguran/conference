package com.conference.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.conference.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventContextDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventContextDTO.class);
        EventContextDTO eventContextDTO1 = new EventContextDTO();
        eventContextDTO1.setId(1L);
        EventContextDTO eventContextDTO2 = new EventContextDTO();
        assertThat(eventContextDTO1).isNotEqualTo(eventContextDTO2);
        eventContextDTO2.setId(eventContextDTO1.getId());
        assertThat(eventContextDTO1).isEqualTo(eventContextDTO2);
        eventContextDTO2.setId(2L);
        assertThat(eventContextDTO1).isNotEqualTo(eventContextDTO2);
        eventContextDTO1.setId(null);
        assertThat(eventContextDTO1).isNotEqualTo(eventContextDTO2);
    }
}
