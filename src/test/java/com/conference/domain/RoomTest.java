package com.conference.domain;

import static com.conference.domain.EventContextTestSamples.*;
import static com.conference.domain.RoomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.conference.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Room.class);
        Room room1 = getRoomSample1();
        Room room2 = new Room();
        assertThat(room1).isNotEqualTo(room2);

        room2.setId(room1.getId());
        assertThat(room1).isEqualTo(room2);

        room2 = getRoomSample2();
        assertThat(room1).isNotEqualTo(room2);
    }

    @Test
    void eventContextTest() {
        Room room = getRoomRandomSampleGenerator();
        EventContext eventContextBack = getEventContextRandomSampleGenerator();

        room.setEventContext(eventContextBack);
        assertThat(room.getEventContext()).isEqualTo(eventContextBack);
        assertThat(eventContextBack.getEventContextRoom()).isEqualTo(room);

        room.eventContext(null);
        assertThat(room.getEventContext()).isNull();
        assertThat(eventContextBack.getEventContextRoom()).isNull();
    }
}
