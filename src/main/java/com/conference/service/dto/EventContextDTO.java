package com.conference.service.dto;

import com.conference.domain.enumeration.EventContextStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.conference.domain.EventContext} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventContextDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private EventContextStatus eventContextStatus;

    private Instant start;

    private Instant end;

    private ApplicationUserDTO contextHost;

    private EventDTO event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventContextStatus getEventContextStatus() {
        return eventContextStatus;
    }

    public void setEventContextStatus(EventContextStatus eventContextStatus) {
        this.eventContextStatus = eventContextStatus;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public ApplicationUserDTO getContextHost() {
        return contextHost;
    }

    public void setContextHost(ApplicationUserDTO contextHost) {
        this.contextHost = contextHost;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventContextDTO)) {
            return false;
        }

        EventContextDTO eventContextDTO = (EventContextDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventContextDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventContextDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", eventContextStatus='" + getEventContextStatus() + "'" +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            ", contextHost=" + getContextHost() +
            ", event=" + getEvent() +
            "}";
    }
}
