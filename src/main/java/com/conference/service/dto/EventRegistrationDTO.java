package com.conference.service.dto;

import com.conference.domain.enumeration.EventRegistrationStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.conference.domain.EventRegistration} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventRegistrationDTO implements Serializable {

    private Long id;

    private String description;

    @NotNull
    private EventRegistrationStatus eventRegistrationStatus;

    private ApplicationUserDTO eventCounterparty;

    private EventContextDTO eventContext;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventRegistrationStatus getEventRegistrationStatus() {
        return eventRegistrationStatus;
    }

    public void setEventRegistrationStatus(EventRegistrationStatus eventRegistrationStatus) {
        this.eventRegistrationStatus = eventRegistrationStatus;
    }

    public ApplicationUserDTO getEventCounterparty() {
        return eventCounterparty;
    }

    public void setEventCounterparty(ApplicationUserDTO eventCounterparty) {
        this.eventCounterparty = eventCounterparty;
    }

    public EventContextDTO getEventContext() {
        return eventContext;
    }

    public void setEventContext(EventContextDTO eventContext) {
        this.eventContext = eventContext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventRegistrationDTO)) {
            return false;
        }

        EventRegistrationDTO eventRegistrationDTO = (EventRegistrationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventRegistrationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventRegistrationDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", eventRegistrationStatus='" + getEventRegistrationStatus() + "'" +
            ", eventCounterparty=" + getEventCounterparty() +
            ", eventContext=" + getEventContext() +
            "}";
    }
}
